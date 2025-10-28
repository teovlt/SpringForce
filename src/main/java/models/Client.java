package models;

public class Client {
    private String firstname;
    private String lastname;
    private Long date_of_birth;
    private Long permit_number;
    private String address;

    public Client(String name, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.date_of_birth = date_of_birth;
        this.permit_number = permit_number;
        this.address = address;
    }

    public String getFirstnameName() {return firstname;}

    public void setFirstnameName(String name) {this.firstname = name;}

    public String getLastname() {return lastname;}

    public void setLastname(String lastname) {this.lastname = lastname;}

    public Long getDate_of_birth() {return date_of_birth;}

    public void setDate_of_birth(Long dateofbirth) {dateofbirth = dateofbirth;}

    public Long getPermit_Number() {return permit_number;}

    public void setPermit_Number(Long permitNumber) {permit_number = permit_number;}

    public String getAddress() {return address;}

    public void setAddress(String address) {address = address;}
}
