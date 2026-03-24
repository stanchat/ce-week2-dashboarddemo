/*
NOTE: This code was generated with AI assistance for training purposes.

This file is part of Atmosera's AI Adoption Training.
It contains intentional code health issues (security vulnerabilities, missing tests,
poor error handling, etc.) designed to help learners practice establishing code health 
baselines and identifying critical issues with AI assistance.

Activity: Code Health Observatory - Metrics, Dashboards, and What Actually Matters
Purpose: Data access layer with critical security vulnerabilities
Focus Area: Security vulnerabilities, SQL injection risks, missing tests

Code Health Issues in This File:
- Multiple SQL injection vulnerabilities (raw SQL with string concatenation)
- No parameterized queries
- Hard-coded database credentials
- Zero unit tests for data access logic
- No connection pooling or resource management
- Missing error handling for database failures
- No logging of database operations
- Direct exposure of database exceptions
- No transaction management
- Missing validation on all inputs
- Code duplication across CRUD operations
*/

using System;
using System.Collections.Generic;
using System.Data.SqlClient;

namespace CoreBillingEngine
{
    public class InvoiceDAO
    {
        // Hard-coded credentials - security issue
        private string connStr = "Server=prod-db;Database=billing;User Id=admin;Password=admin123;";

        // SQL Injection vulnerability - no parameterized query
        public Dictionary<string, object> GetInvoice(string invoiceId)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    // CRITICAL: SQL Injection vulnerability
                    string sql = "SELECT * FROM invoices WHERE invoice_id = '" + invoiceId + "'";
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    SqlDataReader reader = cmd.ExecuteReader();
                    
                    if (reader.Read())
                    {
                        var invoice = new Dictionary<string, object>();
                        invoice["invoice_id"] = reader["invoice_id"];
                        invoice["customer_id"] = reader["customer_id"];
                        invoice["amount"] = reader["amount"];
                        invoice["status"] = reader["status"];
                        invoice["created_date"] = reader["created_date"];
                        return invoice;
                    }
                }
            }
            catch (Exception ex)
            {
                // Poor error handling - exposing internal details
                throw new Exception("Database error: " + ex.Message);
            }
            return null;
        }

        // More SQL injection vulnerabilities
        public List<Dictionary<string, object>> GetInvoicesByCustomer(string customerId)
        {
            var invoices = new List<Dictionary<string, object>>();
            
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    // CRITICAL: SQL Injection vulnerability
                    string sql = "SELECT * FROM invoices WHERE customer_id = '" + customerId + "' ORDER BY created_date DESC";
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    SqlDataReader reader = cmd.ExecuteReader();
                    
                    while (reader.Read())
                    {
                        var invoice = new Dictionary<string, object>();
                        invoice["invoice_id"] = reader["invoice_id"];
                        invoice["customer_id"] = reader["customer_id"];
                        invoice["amount"] = reader["amount"];
                        invoice["status"] = reader["status"];
                        invoice["created_date"] = reader["created_date"];
                        invoices.Add(invoice);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception("Database error: " + ex.Message);
            }
            
            return invoices;
        }

        // No input validation
        public bool CreateInvoice(string customerId, double amount, string status)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    // CRITICAL: SQL Injection vulnerability
                    string sql = "INSERT INTO invoices (customer_id, amount, status, created_date) VALUES ('" 
                        + customerId + "', " + amount + ", '" + status + "', GETDATE())";
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    cmd.ExecuteNonQuery();
                }
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.Message);
                return false;
            }
        }

        // Duplicate code pattern
        public bool UpdateInvoiceStatus(string invoiceId, string newStatus)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    // CRITICAL: SQL Injection vulnerability
                    string sql = "UPDATE invoices SET status = '" + newStatus + "' WHERE invoice_id = '" + invoiceId + "'";
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    cmd.ExecuteNonQuery();
                }
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.Message);
                return false;
            }
        }

        // No transaction management for financial operations
        public bool DeleteInvoice(string invoiceId)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    // CRITICAL: SQL Injection vulnerability
                    string sql = "DELETE FROM invoices WHERE invoice_id = '" + invoiceId + "'";
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    cmd.ExecuteNonQuery();
                }
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.Message);
                return false;
            }
        }

        // Complex query with multiple vulnerabilities
        public List<Dictionary<string, object>> SearchInvoices(string customerName, string status, double minAmount, double maxAmount)
        {
            var invoices = new List<Dictionary<string, object>>();
            
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    // CRITICAL: Multiple SQL Injection vulnerabilities
                    string sql = "SELECT i.*, c.name FROM invoices i JOIN customers c ON i.customer_id = c.customer_id " +
                                 "WHERE c.name LIKE '%" + customerName + "%' " +
                                 "AND i.status = '" + status + "' " +
                                 "AND i.amount >= " + minAmount + " " +
                                 "AND i.amount <= " + maxAmount;
                    
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    SqlDataReader reader = cmd.ExecuteReader();
                    
                    while (reader.Read())
                    {
                        var invoice = new Dictionary<string, object>();
                        invoice["invoice_id"] = reader["invoice_id"];
                        invoice["customer_id"] = reader["customer_id"];
                        invoice["customer_name"] = reader["name"];
                        invoice["amount"] = reader["amount"];
                        invoice["status"] = reader["status"];
                        invoices.Add(invoice);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new Exception("Database error: " + ex.Message);
            }
            
            return invoices;
        }
    }
}
