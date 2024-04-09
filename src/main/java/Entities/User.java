package Entities;
import java.sql.Date;
import java.sql.Timestamp;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private String email;
    private String roles;
    private String password;
    private String cin;
    private  String lastname;
    private String firstname;
    private String gender;
    private Date datebirth;
    private String phone;
    private Timestamp created_at;
    private Boolean is_banned;
    private String profile_picture;
    private Boolean is_verified;
    private String auth_code;


    public User(String email, String roles, String password, String cin, String lastname, String firstname, String gender, Date datebirth, String phone, Timestamp created_at, Boolean is_banned, String profile_picture, Boolean is_verified, String auth_code) {
        this.email = email;
        this.roles = roles;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.cin = cin;
        this.lastname = lastname;
        this.firstname = firstname;
        this.gender = gender;
        this.datebirth = datebirth;
        this.phone = phone;
        this.created_at = created_at;
        this.is_banned = is_banned;
        this.profile_picture = profile_picture;
        this.is_verified = is_verified;
        this.auth_code = auth_code;
    }

    public User(String email, String roles, String password, String lastname, String firstname, String gender, Date datebirth, String phone, Timestamp created_at, Boolean is_banned, String profile_picture, Boolean is_verified, String auth_code) {
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
        this.gender = gender;
        this.datebirth = datebirth;
        this.phone = phone;
        this.created_at = created_at;
        this.is_banned = is_banned;
        this.profile_picture = profile_picture;
        this.is_verified = is_verified;
        this.auth_code = auth_code;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        //this.password = hashedPassword;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(13));
        //this.password = password;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(Date datebirth) {
        this.datebirth = datebirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Boolean getIs_banned() {
        return is_banned;
    }

    public void setIs_banned(Boolean is_banned) {
        this.is_banned = is_banned;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", roles='" + roles + '\'' +
                ", password='" + password + '\'' +
                ", cin='" + cin + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", gender='" + gender + '\'' +
                ", datebirth=" + datebirth +
                ", phone='" + phone + '\'' +
                ", created_at=" + created_at +
                ", is_banned=" + is_banned +
                ", profile_picture='" + profile_picture + '\'' +
                ", is_verified=" + is_verified +
                ", auth_code='" + auth_code + '\'' +
                '}';
    }
}
