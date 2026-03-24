import java.sql.*;
import java.util.*;

public class InvoiceDAO {
    // Hard-coded credentials - security issue
    private String connStr = "jdbc:mysql://prod-db:3306/billing?user=admin&password=admin123";
    
    // SQL Injection vulnerability - no parameterized query
    public Map<String, Object> getInvoice(String invoiceId) {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "SELECT * FROM invoices WHERE invoice_id = '" + invoiceId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("invoice_id", rs.getString("invoice_id"));
                invoice.put("customer_id", rs.getString("customer_id"));
                invoice.put("amount", rs.getDouble("amount"));
                invoice.put("status", rs.getString("status"));
                invoice.put("created_date", rs.getTimestamp("created_date"));
                
                rs.close();
                stmt.close();
                conn.close();
                return invoice;
            }
            
            conn.close();
        } catch (Exception ex) {
            // Poor error handling - exposing internal details
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
        return null;
    }
    
    // More SQL injection vulnerabilities
    public List<Map<String, Object>> getInvoicesByCustomer(String customerId) {
        List<Map<String, Object>> invoices = new ArrayList<>();
        
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "SELECT * FROM invoices WHERE customer_id = '" + customerId + "' ORDER BY created_date DESC";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("invoice_id", rs.getString("invoice_id"));
                invoice.put("customer_id", rs.getString("customer_id"));
                invoice.put("amount", rs.getDouble("amount"));
                invoice.put("status", rs.getString("status"));
                invoice.put("created_date", rs.getTimestamp("created_date"));
                invoices.add(invoice);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
        
        return invoices;
    }
    
    // No input validation
    public boolean createInvoice(String customerId, double amount, String status) {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "INSERT INTO invoices (customer_id, amount, status, created_date) VALUES ('" 
                + customerId + "', " + amount + ", '" + status + "', NOW())";
            stmt.executeUpdate(sql);
            
            stmt.close();
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    // Duplicate code pattern
    public boolean updateInvoiceStatus(String invoiceId, String newStatus) {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "UPDATE invoices SET status = '" + newStatus + "' WHERE invoice_id = '" + invoiceId + "'";
            stmt.executeUpdate(sql);
            
            stmt.close();
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    // No transaction management for financial operations
    public boolean deleteInvoice(String invoiceId) {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "DELETE FROM invoices WHERE invoice_id = '" + invoiceId + "'";
            stmt.executeUpdate(sql);
            
            stmt.close();
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    // Complex query with multiple vulnerabilities
    public List<Map<String, Object>> searchInvoices(String customerName, String status, 
                                                     double minAmount, double maxAmount) {
        List<Map<String, Object>> invoices = new ArrayList<>();
        
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: Multiple SQL Injection vulnerabilities
            String sql = "SELECT i.*, c.name FROM invoices i JOIN customers c ON i.customer_id = c.customer_id " +
                         "WHERE c.name LIKE '%" + customerName + "%' " +
                         "AND i.status = '" + status + "' " +
                         "AND i.amount >= " + minAmount + " " +
                         "AND i.amount <= " + maxAmount;
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("invoice_id", rs.getString("invoice_id"));
                invoice.put("customer_id", rs.getString("customer_id"));
                invoice.put("customer_name", rs.getString("name"));
                invoice.put("amount", rs.getDouble("amount"));
                invoice.put("status", rs.getString("status"));
                invoices.add(invoice);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
        
        return invoices;
    }
}
