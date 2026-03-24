import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class PaymentProcessor {
    // Hard-coded credentials - security issue
    private String connStr = "jdbc:mysql://prod-db:3306/billing?user=admin&password=admin123";
    private List<Thread> threads = new ArrayList<>();
    
    // Cyclomatic complexity: 42 (way above threshold of 10-15)
    public boolean processPayment(String customerId, double amount, String method, Map<String, String> metadata) {
        // No input validation
        if (amount > 0) {
            if (method.equals("credit_card")) {
                if (metadata.containsKey("card_number")) {
                    String cardNum = metadata.get("card_number");
                    if (cardNum.length() == 16) {
                        if (cardNum.startsWith("4") || cardNum.startsWith("5")) {
                            // Deeply nested - 6 levels deep
                            if (amount < 100) {
                                return processSmallPayment(customerId, amount, cardNum);
                            } else if (amount < 1000) {
                                return processMediumPayment(customerId, amount, cardNum);
                            } else {
                                if (metadata.containsKey("authorization_code")) {
                                    return processLargePayment(customerId, amount, cardNum, 
                                        metadata.get("authorization_code"));
                                } else {
                                    System.out.println("Missing auth code");
                                    return false;
                                }
                            }
                        } else {
                            System.out.println("Unsupported card type");
                            return false;
                        }
                    } else {
                        System.out.println("Invalid card length");
                        return false;
                    }
                } else {
                    System.out.println("No card number");
                    return false;
                }
            } else if (method.equals("bank_transfer")) {
                if (metadata.containsKey("account_number")) {
                    if (metadata.containsKey("routing_number")) {
                        return processBankTransfer(customerId, amount, 
                            metadata.get("account_number"), metadata.get("routing_number"));
                    } else {
                        System.out.println("No routing number");
                        return false;
                    }
                } else {
                    System.out.println("No account number");
                    return false;
                }
            } else if (method.equals("paypal")) {
                if (metadata.containsKey("email")) {
                    return processPayPal(customerId, amount, metadata.get("email"));
                } else {
                    System.out.println("No PayPal email");
                    return false;
                }
            } else {
                System.out.println("Unknown payment method");
                return false;
            }
        } else {
            System.out.println("Invalid amount");
            return false;
        }
    }
    
    // SQL Injection vulnerability - using raw SQL with string concatenation
    private boolean processSmallPayment(String customerId, double amount, String cardNum) {
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "INSERT INTO payments (customer_id, amount, card_last4, status) VALUES ('" 
                + customerId + "', " + amount + ", '" + cardNum.substring(12) + "', 'completed')";
            stmt.executeUpdate(sql);
            
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    // Manual thread management - infrastructure mixed with business logic
    private boolean processMediumPayment(String customerId, double amount, String cardNum) {
        final boolean[] success = {false};
        
        Thread t = new Thread(() -> {
            try {
                // Simulate async processing
                Thread.sleep(100);
                success[0] = processSmallPayment(customerId, amount, cardNum);
                
                // Magic number - 30 second delay
                if (success[0]) {
                    Thread.sleep(30 * 1000);
                    sendConfirmationEmail(customerId, amount);
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        });
        
        threads.add(t);
        t.start();
        
        try {
            t.join(5000); // Magic number - 5 second timeout
        } catch (InterruptedException ex) {
            System.out.println("Thread interrupted");
        }
        
        return success[0];
    }
    
    private boolean processLargePayment(String customerId, double amount, String cardNum, String authCode) {
        // Duplicate validation logic
        if (authCode.length() != 6) {
            System.out.println("Invalid auth code");
            return false;
        }
        
        // More SQL injection
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "INSERT INTO payments (customer_id, amount, card_last4, auth_code, status) VALUES ('" 
                + customerId + "', " + amount + ", '" + cardNum.substring(12) + "', '" 
                + authCode + "', 'pending_review')";
            stmt.executeUpdate(sql);
            
            conn.close();
            
            // No retry logic for transient failures
            notifyFraudTeam(customerId, amount);
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    private boolean processBankTransfer(String customerId, double amount, String accountNum, String routingNum) {
        // Duplicate code pattern from credit card processing
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "INSERT INTO payments (customer_id, amount, account_last4, routing, status) VALUES ('" 
                + customerId + "', " + amount + ", '" 
                + accountNum.substring(accountNum.length() - 4) + "', '" 
                + routingNum + "', 'processing')";
            stmt.executeUpdate(sql);
            
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    private boolean processPayPal(String customerId, double amount, String email) {
        // More duplicate code
        try {
            Connection conn = DriverManager.getConnection(connStr);
            Statement stmt = conn.createStatement();
            
            // CRITICAL: SQL Injection vulnerability
            String sql = "INSERT INTO payments (customer_id, amount, paypal_email, status) VALUES ('" 
                + customerId + "', " + amount + ", '" + email + "', 'completed')";
            stmt.executeUpdate(sql);
            
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    private void sendConfirmationEmail(String customerId, double amount) {
        // Stub - no implementation
        System.out.println("Email sent to " + customerId + " for $" + amount);
    }
    
    private void notifyFraudTeam(String customerId, double amount) {
        // Stub - no implementation
        System.out.println("Fraud team notified about $" + amount + " payment from " + customerId);
    }
}
