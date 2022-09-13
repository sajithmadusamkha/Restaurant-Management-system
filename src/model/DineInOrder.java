package model;

public class DineInOrder {
    String id;
    String cusId;
    String empId;
    String tableId;

    public DineInOrder() {
    }

    public DineInOrder(String id, String cusId, String empId, String tableId) {
        this.id = id;
        this.cusId = cusId;
        this.empId = empId;
        this.tableId = tableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Override
    public String toString() {
        return "DineInOrder{" +
                "id='" + id + '\'' +
                ", cusId='" + cusId + '\'' +
                ", empId='" + empId + '\'' +
                ", tableId='" + tableId + '\'' +
                '}';
    }
}
