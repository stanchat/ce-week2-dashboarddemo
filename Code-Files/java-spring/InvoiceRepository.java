/*
NOTE: This code was generated with AI assistance for training purposes.

This file is part of Atmosera's AI Adoption Training.
It contains intentional code health issues (security vulnerabilities, missing tests,
poor error handling, etc.) designed to help learners practice establishing code health 
baselines and identifying critical issues with AI assistance.

Activity: Code Health Observatory - Metrics, Dashboards, and What Actually Matters
Purpose: Data access repository with critical security vulnerabilities
Focus Area: Security vulnerabilities, SQL injection risks, missing tests

Code Health Issues in This File:
- Multiple SQL injection vulnerabilities (raw SQL with string concatenation)
- No parameterized queries using Spring JDBC properly
- Zero unit tests for data access logic
- No use of Spring Data JPA or proper ORM
- Missing error handling for database failures
- No logging of database operations
- Direct exposure of database exceptions
- No Spring transaction management
- Missing validation on all inputs
- Code duplication across CRUD operations
- Not using repository pattern properly
*/

package com.atmosera.billing.repository;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Repository
public class InvoiceRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // SQL Injection vulnerability - no parameterized query
    public Map<String, Object> getInvoice(String invoiceId) {
        try {
            // CRITICAL: SQL Injection vulnerability - not using PreparedStatement
            String sql = "SELECT * FROM invoices WHERE invoice_id = '" + invoiceId + "'";
            
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            
            if (!results.isEmpty()) {
                return results.get(0);
            }
        } catch (Exception ex) {
            // Poor error handling - exposing internal details
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
        return null;
    }
    
    // More SQL injection vulnerabilities
    public List<Map<String, Object>> getInvoicesByCustomer(String customerId) {
        try {
            // CRITICAL: SQL Injection vulnerability
            String sql = "SELECT * FROM invoices WHERE customer_id = '" + customerId + "' ORDER BY created_date DESC";
            return jdbcTemplate.queryForList(sql);
        } catch (Exception ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }
    
    // No input validation
    public boolean createInvoice(String customerId, double amount, String status) {
        try {
            // CRITICAL: SQL Injection vulnerability
            String sql = "INSERT INTO invoices (customer_id, amount, status, created_date) VALUES ('" 
                + customerId + "', " + amount + ", '" + status + "', NOW())";
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    // Duplicate code pattern
    public boolean updateInvoiceStatus(String invoiceId, String newStatus) {
        try {
            // CRITICAL: SQL Injection vulnerability
            String sql = "UPDATE invoices SET status = '" + newStatus + "' WHERE invoice_id = '" + invoiceId + "'";
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    // No transaction management for financial operations
    public boolean deleteInvoice(String invoiceId) {
        try {
            // CRITICAL: SQL Injection vulnerability
            String sql = "DELETE FROM invoices WHERE invoice_id = '" + invoiceId + "'";
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }
    
    // Complex query with multiple vulnerabilities
    public List<Map<String, Object>> searchInvoices(String customerName, String status, 
                                                     double minAmount, double maxAmount) {
        try {
            // CRITICAL: Multiple SQL Injection vulnerabilities
            String sql = "SELECT i.*, c.name FROM invoices i JOIN customers c ON i.customer_id = c.customer_id " +
                         "WHERE c.name LIKE '%" + customerName + "%' " +
                         "AND i.status = '" + status + "' " +
                         "AND i.amount >= " + minAmount + " " +
                         "AND i.amount <= " + maxAmount;
            
            return jdbcTemplate.queryForList(sql);
        } catch (Exception ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }
}
