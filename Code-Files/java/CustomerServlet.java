import java.util.*;
import java.util.regex.*;

public class CustomerServlet {
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private Map<String, Map<String, Object>> customers = new HashMap<>();
    
    // CRITICAL: Zero tests on this validation logic
    public boolean createCustomer(String customerId, String name, String email, 
                                  String phone, int age, double creditLimit) {
        // No input validation at all
        Map<String, Object> customer = new HashMap<>();
        customer.put("customer_id", customerId);
        customer.put("name", name);
        customer.put("email", email);
        customer.put("phone", phone);
        customer.put("age", age);
        customer.put("credit_limit", creditLimit);
        customer.put("status", "active");
        customer.put("created_date", new Date());
        
        customers.put(customerId, customer);
        return true;
    }
    
    // No validation of email format
    public boolean updateEmail(String customerId, String newEmail) {
        if (customers.containsKey(customerId)) {
            customers.get(customerId).put("email", newEmail);
            return true;
        }
        return false;
    }
    
    // No boundary checks on credit limit
    public boolean updateCreditLimit(String customerId, double newLimit) {
        if (customers.containsKey(customerId)) {
            // Magic numbers - what's the business rule?
            if (newLimit > 0) {
                customers.get(customerId).put("credit_limit", newLimit);
                return true;
            }
        }
        return false;
    }
    
    // CRITICAL: Complex business logic with zero tests
    public boolean approveCreditIncrease(String customerId, double requestedIncrease) {
        if (!customers.containsKey(customerId)) {
            return false;
        }
        
        Map<String, Object> customer = customers.get(customerId);
        double currentLimit = (Double) customer.get("credit_limit");
        int age = (Integer) customer.get("age");
        
        // Complex business rules with no tests
        if (age < 18) {
            return false; // Minors cannot get credit increases
        }
        
        if (requestedIncrease > 5000) {
            // Large increases require manual approval
            return false;
        }
        
        if (currentLimit + requestedIncrease > 10000) {
            // Total limit cannot exceed $10,000
            return false;
        }
        
        // Check payment history
        List<Map<String, Object>> invoices = invoiceDAO.getInvoicesByCustomer(customerId);
        int latePayments = 0;
        for (Map<String, Object> invoice : invoices) {
            if ("late".equals(invoice.get("status"))) {
                latePayments++;
            }
        }
        
        if (latePayments > 2) {
            // Too many late payments
            return false;
        }
        
        // Approve the increase
        customer.put("credit_limit", currentLimit + requestedIncrease);
        return true;
    }
    
    // Missing validation on phone format
    public boolean updatePhone(String customerId, String newPhone) {
        if (customers.containsKey(customerId)) {
            customers.get(customerId).put("phone", newPhone);
            return true;
        }
        return false;
    }
    
    // No error handling for missing customer
    public Map<String, Object> getCustomer(String customerId) {
        return customers.get(customerId); // Will return null if customer doesn't exist
    }
    
    // CRITICAL: Account closure logic with zero tests
    public boolean closeAccount(String customerId, String reason) {
        if (!customers.containsKey(customerId)) {
            return false;
        }
        
        Map<String, Object> customer = customers.get(customerId);
        
        // Check for outstanding invoices
        List<Map<String, Object>> invoices = invoiceDAO.getInvoicesByCustomer(customerId);
        double totalOutstanding = 0;
        
        for (Map<String, Object> invoice : invoices) {
            String status = (String) invoice.get("status");
            if ("pending".equals(status) || "late".equals(status)) {
                totalOutstanding += (Double) invoice.get("amount");
            }
        }
        
        if (totalOutstanding > 0) {
            // Cannot close account with outstanding balance
            return false;
        }
        
        // Close the account
        customer.put("status", "closed");
        customer.put("closed_date", new Date());
        customer.put("close_reason", reason);
        
        return true;
    }
    
    // Duplicate validation logic - should be extracted
    public boolean validateCustomerData(String name, String email, String phone, int age) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        
        if (age < 0 || age > 150) {
            return false;
        }
        
        return true;
    }
    
    // No tests for this calculation logic
    public double calculateAvailableCredit(String customerId) {
        if (!customers.containsKey(customerId)) {
            return 0;
        }
        
        Map<String, Object> customer = customers.get(customerId);
        double creditLimit = (Double) customer.get("credit_limit");
        
        List<Map<String, Object>> invoices = invoiceDAO.getInvoicesByCustomer(customerId);
        double totalUsed = 0;
        
        for (Map<String, Object> invoice : invoices) {
            String status = (String) invoice.get("status");
            if (!"paid".equals(status) && !"cancelled".equals(status)) {
                totalUsed += (Double) invoice.get("amount");
            }
        }
        
        return creditLimit - totalUsed;
    }
}
