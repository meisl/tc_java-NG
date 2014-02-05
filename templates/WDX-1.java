 extends ContentPlugin {

    protected void initFields() {

        define(new Field.INTEGER("foo") { public Integer getValue(String fileName) throws IOException {
            return fileName.length();
        }});

        define(new Field.STRING("bar") { public String getValue(String fileName) throws IOException {
            return "baz";
        }});

    }

    public static void main(String... args) throws IOException {
        new 