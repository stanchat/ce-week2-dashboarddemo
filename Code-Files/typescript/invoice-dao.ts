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
- Using any types throughout
*/

interface Invoice {
    invoice_id: string;
    customer_id: string;
    amount: number;
    status: string;
    created_date: Date;
}

class InvoiceDAO {
    // Hard-coded credentials - security issue
    private connStr: string = "postgresql://admin:admin123@prod-db:5432/billing";
    
    // SQL Injection vulnerability - no parameterized query
    getInvoice(invoiceId: string): Invoice | null {
        try {
            // CRITICAL: SQL Injection vulnerability (simulated)
            const sql = `SELECT * FROM invoices WHERE invoice_id = '${invoiceId}'`;
            
            // Simulated database call
            console.log("Executing: " + sql);
            
            // Mock return
            return {
                invoice_id: invoiceId,
                customer_id: "CUST001",
                amount: 100.00,
                status: "pending",
                created_date: new Date()
            };
        } catch (ex: any) {
            // Poor error handling - exposing internal details
            throw new Error("Database error: " + ex.message);
        }
    }
    
    // More SQL injection vulnerabilities
    getInvoicesByCustomer(customerId: string): Invoice[] {
        const invoices: Invoice[] = [];
        
        try {
            // CRITICAL: SQL Injection vulnerability
            const sql = `SELECT * FROM invoices WHERE customer_id = '${customerId}' ORDER BY created_date DESC`;
            
            console.log("Executing: " + sql);
            
            // Mock return
            return invoices;
        } catch (ex: any) {
            throw new Error("Database error: " + ex.message);
        }
    }
    
    // No input validation
    createInvoice(customerId: string, amount: number, status: string): boolean {
        try {
            // CRITICAL: SQL Injection vulnerability
            const sql = `INSERT INTO invoices (customer_id, amount, status, created_date) VALUES ('${customerId}', ${amount}, '${status}', NOW())`;
            
            console.log("Executing: " + sql);
            return true;
        } catch (ex: any) {
            console.log("Error: " + ex.message);
            return false;
        }
    }
    
    // Duplicate code pattern
    updateInvoiceStatus(invoiceId: string, newStatus: string): boolean {
        try {
            // CRITICAL: SQL Injection vulnerability
            const sql = `UPDATE invoices SET status = '${newStatus}' WHERE invoice_id = '${invoiceId}'`;
            
            console.log("Executing: " + sql);
            return true;
        } catch (ex: any) {
            console.log("Error: " + ex.message);
            return false;
        }
    }
    
    // No transaction management for financial operations
    deleteInvoice(invoiceId: string): boolean {
        try {
            // CRITICAL: SQL Injection vulnerability
            const sql = `DELETE FROM invoices WHERE invoice_id = '${invoiceId}'`;
            
            console.log("Executing: " + sql);
            return true;
        } catch (ex: any) {
            console.log("Error: " + ex.message);
            return false;
        }
    }
    
    // Complex query with multiple vulnerabilities
    searchInvoices(customerName: string, status: string, minAmount: number, maxAmount: number): any[] {
        const invoices: any[] = [];
        
        try {
            // CRITICAL: Multiple SQL Injection vulnerabilities
            const sql = `SELECT i.*, c.name FROM invoices i JOIN customers c ON i.customer_id = c.customer_id ` +
                       `WHERE c.name LIKE '%${customerName}%' ` +
                       `AND i.status = '${status}' ` +
                       `AND i.amount >= ${minAmount} ` +
                       `AND i.amount <= ${maxAmount}`;
            
            console.log("Executing: " + sql);
            
            return invoices;
        } catch (ex: any) {
            throw new Error("Database error: " + ex.message);
        }
    }
}

export default InvoiceDAO;
