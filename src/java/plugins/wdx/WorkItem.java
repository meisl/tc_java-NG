package plugins.wdx;


import java.util.*;

import static plugins.wdx.ContentPlugin.*;


public class WorkItem {

    public static class Store {

        static final class NullStore extends Store {

            @Override
            protected void init() {
                this.fileNames2items = null;
            }

            /* @throws UnsupportedOperationException
             */
            @Override
            public WorkItem newItem(String fileName, Field<?> field, int unitIndex) {
                throw new UnsupportedOperationException("WorkItem.Store.NULL cannot create WorkItems");
            }

            private final synchronized void add(WorkItem it) {
                // do nothing;
            }

            private synchronized Set<WorkItem> getItems(String fileName) {
                return Collections.<WorkItem>emptySet();
            }

            @Override
            public Iterable<String> fileNames() {
                return Collections.<String>emptyList();
            }

        }

        /* Discards any WorkItems added to it and cannot create new WorkItems.
         */
        public static final NullStore NULL = new NullStore();


        /* ----- WorkItem.Store: non-static members ---------------------------- */


        protected Map<String, Set<WorkItem>> fileNames2items;

        public Store() {
            init();
        }

        protected void init() {
            this.fileNames2items = new HashMap<>();
        }

        /* Creates a new WorkItem in this store and returns it.
         */
        public WorkItem newItem(String fileName, Field<?> field, int unitIndex) {
            WorkItem it = new WorkItem(fileName, field, unitIndex);
            this.add(it);
            return it;
        }

        /* Adds the specified WorkItem to this store.
         * @throws IllegalArgumentException if the item already is in this store.
         */
        private synchronized void add(WorkItem it) {
            if (it.store == this) {
                throw new IllegalArgumentException("cannot add WorkItem again: " + it);
            }
            Set<WorkItem> items = this.getItems(it.fileName);
            if (items.isEmpty()) {
                items = new HashSet<>();
                fileNames2items.put(it.fileName, items);
            }
            items.add(it);
            it.setStore(this);
        }

        /* Removes the specified WorkItem from this store.
         * @throws IllegalArgumentException if the item was not in this store.
         */
        private synchronized void remove(WorkItem it) {
            if (it.store != this) {
                throw new IllegalArgumentException("cannot remove - no such WorkItem: " + it);
            }
            Set<WorkItem> items = this.getItems(it.fileName);
            items.remove(it);
            if (items.isEmpty()) {
                fileNames2items.remove(it.fileName);
            }
            it.setStore(null);
        }
/*
    WorkItem addWorkItem(String fileName, Field<?> field, int unitIndex) {
        synchronized (fileNames2workItems) {
            WorkItem result = new WorkItem(fileName, field, unitIndex);
            HashSet<WorkItem> workItems = fileNames2workItems.get(fileName);
            if (workItems == null) {
                workItems = new HashSet<>();
                fileNames2workItems.put(fileName, workItems);
            }
            workItems.add(result);
            return result;
        }
    }
    
    void removeWorkItem(WorkItem workItem) {
        synchronized (fileNames2workItems) {
            Set<WorkItem> workItems = fileNames2workItems.get(workItem.fileName);
            workItems.remove(workItem);
            if (workItems.isEmpty()) {
                fileNames2workItems.remove(workItem.fileName);
            }
        }
    }
*/
        private synchronized Set<WorkItem> getItems(String fileName) {
            Set<WorkItem> workItems = fileNames2items.get(fileName);
            return (workItems == null) ? Collections.<WorkItem>emptySet() : workItems;
        }

        Iterable<String> fileNames() {
            return fileNames2items.keySet();
        }

        Iterable<WorkItem> items(String fileName) {
            return this.getItems(fileName);
        }

        public synchronized int itemCount(String fileName) {
            return this.getItems(fileName).size();
        }

        public synchronized String toString() {
            StringBuilder result = new StringBuilder();
            for (String fileName: this.fileNames()) {
                Set<WorkItem> workItems = this.getItems(fileName);
                result.append("\n")
                    .append(workItems.size())
                    .append(": ")
                    .append(fileName);
                for (WorkItem it: workItems) {
                    result.append("\n    ")
                        .append(it.field.name)
                        .append(".")
                        .append(it.unitIndex)
                        .append("  ")
                        .append(it.getTime())
                        .append(" ms");
                    if (it.workingThread.isInterrupted()) {
                        result.append("  *");
                    }
                }
            }
            return result.toString();
        }

    }


    /* ----- WorkItem: non-static members -------------------------------------- */


    private Thread workingThread;
    private String fileName;
    private Field<?> field;
    private int unitIndex;
    private long time;
    private Store store;

    WorkItem(String fileName, Field<?> field, int unitIndex) {
        this.store = store;
        this.workingThread = Thread.currentThread();
        this.fileName = fileName;
        this.field = field;
        this.unitIndex = unitIndex;
        //myLog.warn("start slow " + field.name + ": \"" + fileName + "\"");    // TODO: logging
        this.time = -System.currentTimeMillis();
    }

    private void setStore(Store store) {
        this.store = store;
    }

    public void requestStop() {
        this.workingThread.interrupt();
    }

    public long getTime() {
        return this.time >= 0 ? this.time : this.time + System.currentTimeMillis();
    }

    public void cleanup() {
        this.time = getTime();
        if (this.workingThread != Thread.currentThread()) { // TODO: assert it
            throw new IllegalStateException(this.workingThread + " != " + Thread.currentThread());
        }
        Thread.interrupted(); // clear interrupted status
        this.store.remove(this);
    }

}
