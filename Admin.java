public class Admin extends User {
    private String adminRole;

    public Admin(Long id, String name, String email, String password, String adminRole) {
        super(id, name, email, password);
        this.adminRole = adminRole;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }
}
