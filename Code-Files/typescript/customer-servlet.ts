/*
NOTE: This code was generated with AI assistance for training purposes.

This file is part of Atmosera's AI Adoption Training.
It contains intentional code health issues (zero test coverage, missing validation,
poor error handling, etc.) designed to help learners practice establishing code health 
baselines and identifying critical testing gaps with AI assistance.

Activity: Code Health Observatory - Metrics, Dashboards, and What Actually Matters
Purpose: Customer service layer with critical testing gaps
Focus Area: Test coverage gaps, missing validation, error handling

Code Health Issues in This File:
- Zero unit tests on critical validation logic (0% coverage)
- No input validation on customer data
- Missing error handling for null/invalid inputs
- No validation of email format
- No validation of phone number format
- No boundary checks on age/credit limits
- Direct exposure of internal exceptions
- No logging of customer operations
- Magic numbers for credit limits (5000, 10000)
- Duplicate validation logic across methods
- No data sanitization before storage
- Missing business rule validations
- Using any types throughout
*/

import InvoiceDAO from './invoice-dao';

interface Customer {
    customer_id: string;
    name: string;
    email: string;
    phone: string;
    age: number;
    credit_limit: number;
    status: string;
    created_date: Date;
    closed_date?: Date;
    close_reason?: string;
}

class CustomerServlet {
    private invoiceDAO: InvoiceDAO;
    private customers: Map<string, Customer>;
    
    constructor() {
        this.invoiceDAO = new InvoiceDAO();
        this.customers = new Map();
    }
    
    // CRITICAL: Zero tests on this validation logic
    createCustomer(customerId: string, name: string, email: string, 
                  phone: string, age: number, creditLimit: number): boolean {
        // No input validation at all
        const customer: Customer = {
            customer_id: customerId,
            name: name,
            email: email,
            phone: phone,
            age: age,
            credit_limit: creditLimit,
            status: "active",
            created_date: new Date()
        };
        
        this.customers.set(customerId, customer);
        return true;
    }
    
    // No validation of email format
    updateEmail(customerId: string, newEmail: string): boolean {
        if (this.customers.has(customerId)) {
            const customer = this.customers.get(customerId)!;
            customer.email = newEmail;
            return true;
        }
        return false;
    }
    
    // No boundary checks on credit limit
    updateCreditLimit(customerId: string, newLimit: number): boolean {
        if (this.customers.has(customerId)) {
            // Magic numbers - what's the business rule?
            if (newLimit > 0) {
                const customer = this.customers.get(customerId)!;
                customer.credit_limit = newLimit;
                return true;
            }
        }
        return false;
    }
    
    // CRITICAL: Complex business logic with zero tests
    approveCreditIncrease(customerId: string, requestedIncrease: number): boolean {
        if (!this.customers.has(customerId)) {
            return false;
        }
        
        const customer = this.customers.get(customerId)!;
        const currentLimit = customer.credit_limit;
        const age = customer.age;
        
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
        const invoices = this.invoiceDAO.getInvoicesByCustomer(customerId);
        let latePayments = 0;
        for (const invoice of invoices) {
            if (invoice.status === "late") {
                latePayments++;
            }
        }
        
        if (latePayments > 2) {
            // Too many late payments
            return false;
        }
        
        // Approve the increase
        customer.credit_limit = currentLimit + requestedIncrease;
        return true;
    }
    
    // Missing validation on phone format
    updatePhone(customerId: string, newPhone: string): boolean {
        if (this.customers.has(customerId)) {
            const customer = this.customers.get(customerId)!;
            customer.phone = newPhone;
            return true;
        }
        return false;
    }
    
    // No error handling for missing customer
    getCustomer(customerId: string): Customer | undefined {
        return this.customers.get(customerId); // Will return undefined if customer doesn't exist
    }
    
    // CRITICAL: Account closure logic with zero tests
    closeAccount(customerId: string, reason: string): boolean {
        if (!this.customers.has(customerId)) {
            return false;
        }
        
        const customer = this.customers.get(customerId)!;
        
        // Check for outstanding invoices
        const invoices = this.invoiceDAO.getInvoicesByCustomer(customerId);
        let totalOutstanding = 0;
        
        for (const invoice of invoices) {
            const status = invoice.status;
            if (status === "pending" || status === "late") {
                totalOutstanding += invoice.amount;
            }
        }
        
        if (totalOutstanding > 0) {
            // Cannot close account with outstanding balance
            return false;
        }
        
        // Close the account
        customer.status = "closed";
        customer.closed_date = new Date();
        customer.close_reason = reason;
        
        return true;
    }
    
    // Duplicate validation logic - should be extracted
    validateCustomerData(name: string, email: string, phone: string, age: number): boolean {
        if (!name || name.length === 0) {
            return false;
        }
        
        if (!email || email.length === 0) {
            return false;
        }
        
        if (!phone || phone.length === 0) {
            return false;
        }
        
        if (age < 0 || age > 150) {
            return false;
        }
        
        return true;
    }
    
    // No tests for this calculation logic
    calculateAvailableCredit(customerId: string): number {
        if (!this.customers.has(customerId)) {
            return 0;
        }
        
        const customer = this.customers.get(customerId)!;
        const creditLimit = customer.credit_limit;
        
        const invoices = this.invoiceDAO.getInvoicesByCustomer(customerId);
        let totalUsed = 0;
        
        for (const invoice of invoices) {
            const status = invoice.status;
            if (status !== "paid" && status !== "cancelled") {
                totalUsed += invoice.amount;
            }
        }
        
        return creditLimit - totalUsed;
    }
}

export default CustomerServlet;
