package com.example.demo.VO;

import lombok.Data;

@Data
public class ETC {
    public String No;
    public String Group;
    public String Company;
    public String Name;
    public String Fax;
    public String Status;
    public String Status_Detail;
    public String Done_Date;

    @Override
    public String toString() {
        return "ETC{" +
                "No='" + No + '\'' +
                ", Group='" + Group + '\'' +
                ", Company='" + Company + '\'' +
                ", Name='" + Name + '\'' +
                ", Fax='" + Fax + '\'' +
                ", Status='" + Status + '\'' +
                ", Status_Detail='" + Status_Detail + '\'' +
                ", Done_Date='" + Done_Date + '\'' +
                '}';
    }
}
