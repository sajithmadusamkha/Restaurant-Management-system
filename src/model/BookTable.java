package model;

public class BookTable {
    String id;
    String tableName;
    String status;

    public BookTable() {
    }

    public BookTable(String id, String tableName, String status) {
        this.id = id;
        this.tableName = tableName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BookTable{" +
                "id='" + id + '\'' +
                ", tableName='" + tableName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
