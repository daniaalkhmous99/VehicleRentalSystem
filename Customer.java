public class Customer {

    private String customerId;
    private String fullName;
    private String phone;
    private String email;
    private String licenseNumber;

    public Customer(String customerId, String fullName, String phone,
                    String email, String licenseNumber) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be empty.");
        }
        this.customerId = customerId.trim();
        this.fullName = fullName.trim();
        this.licenseNumber = licenseNumber.trim();
        setPhone(phone);   // setters check the values
        setEmail(email);
    }

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getLicenseNumber() { return licenseNumber; }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().length() < 7) {
            throw new IllegalArgumentException("Phone number must have at least 7 characters.");
        }
        this.phone = phone.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email is not valid (it must contain @ and .).");
        }
        this.email = email.trim();
    }

    public void displayInfo() {
        System.out.println("ID: " + customerId + " | Name: " + fullName + " | Phone: " + phone
                + " | Email: " + email + " | License: " + licenseNumber);
    }
}
