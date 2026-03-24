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
*/

using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace CoreBillingEngine
{
    public class CustomerServlet
    {
        private InvoiceDAO invoiceDAO = new InvoiceDAO();
        private Dictionary<string, Dictionary<string, object>> customers = new Dictionary<string, Dictionary<string, object>>();

        // CRITICAL: Zero tests on this validation logic
        public bool CreateCustomer(string customerId, string name, string email, string phone, int age, double creditLimit)
        {
            // No input validation at all
            var customer = new Dictionary<string, object>();
            customer["customer_id"] = customerId;
            customer["name"] = name;
            customer["email"] = email;
            customer["phone"] = phone;
            customer["age"] = age;
            customer["credit_limit"] = creditLimit;
            customer["status"] = "active";
            customer["created_date"] = DateTime.Now;
            
            customers[customerId] = customer;
            return true;
        }

        // No validation of email format
        public bool UpdateEmail(string customerId, string newEmail)
        {
            if (customers.ContainsKey(customerId))
            {
                customers[customerId]["email"] = newEmail;
                return true;
            }
            return false;
        }

        // No boundary checks on credit limit
        public bool UpdateCreditLimit(string customerId, double newLimit)
        {
            if (customers.ContainsKey(customerId))
            {
                // Magic numbers - what's the business rule?
                if (newLimit > 0)
                {
                    customers[customerId]["credit_limit"] = newLimit;
                    return true;
                }
            }
            return false;
        }

        // CRITICAL: Complex business logic with zero tests
        public bool ApproveCreditIncrease(string customerId, double requestedIncrease)
        {
            if (!customers.ContainsKey(customerId))
            {
                return false;
            }

            var customer = customers[customerId];
            double currentLimit = (double)customer["credit_limit"];
            int age = (int)customer["age"];
            
            // Complex business rules with no tests
            if (age < 18)
            {
                return false; // Minors cannot get credit increases
            }
            
            if (requestedIncrease > 5000)
            {
                // Large increases require manual approval
                return false;
            }
            
            if (currentLimit + requestedIncrease > 10000)
            {
                // Total limit cannot exceed $10,000
                return false;
            }
            
            // Check payment history
            var invoices = invoiceDAO.GetInvoicesByCustomer(customerId);
            int latePayments = 0;
            foreach (var invoice in invoices)
            {
                if (invoice["status"].ToString() == "late")
                {
                    latePayments++;
                }
            }
            
            if (latePayments > 2)
            {
                // Too many late payments
                return false;
            }
            
            // Approve the increase
            customer["credit_limit"] = currentLimit + requestedIncrease;
            return true;
        }

        // Missing validation on phone format
        public bool UpdatePhone(string customerId, string newPhone)
        {
            if (customers.ContainsKey(customerId))
            {
                customers[customerId]["phone"] = newPhone;
                return true;
            }
            return false;
        }

        // No error handling for missing customer
        public Dictionary<string, object> GetCustomer(string customerId)
        {
            return customers[customerId]; // Will throw if customer doesn't exist
        }

        // CRITICAL: Account closure logic with zero tests
        public bool CloseAccount(string customerId, string reason)
        {
            if (!customers.ContainsKey(customerId))
            {
                return false;
            }

            var customer = customers[customerId];
            
            // Check for outstanding invoices
            var invoices = invoiceDAO.GetInvoicesByCustomer(customerId);
            double totalOutstanding = 0;
            
            foreach (var invoice in invoices)
            {
                string status = invoice["status"].ToString();
                if (status == "pending" || status == "late")
                {
                    totalOutstanding += (double)invoice["amount"];
                }
            }
            
            if (totalOutstanding > 0)
            {
                // Cannot close account with outstanding balance
                return false;
            }
            
            // Close the account
            customer["status"] = "closed";
            customer["closed_date"] = DateTime.Now;
            customer["close_reason"] = reason;
            
            return true;
        }

        // Duplicate validation logic - should be extracted
        public bool ValidateCustomerData(string name, string email, string phone, int age)
        {
            if (string.IsNullOrEmpty(name))
            {
                return false;
            }
            
            if (string.IsNullOrEmpty(email))
            {
                return false;
            }
            
            if (string.IsNullOrEmpty(phone))
            {
                return false;
            }
            
            if (age < 0 || age > 150)
            {
                return false;
            }
            
            return true;
        }

        // No tests for this calculation logic
        public double CalculateAvailableCredit(string customerId)
        {
            if (!customers.ContainsKey(customerId))
            {
                return 0;
            }

            var customer = customers[customerId];
            double creditLimit = (double)customer["credit_limit"];
            
            var invoices = invoiceDAO.GetInvoicesByCustomer(customerId);
            double totalUsed = 0;
            
            foreach (var invoice in invoices)
            {
                string status = invoice["status"].ToString();
                if (status != "paid" && status != "cancelled")
                {
                    totalUsed += (double)invoice["amount"];
                }
            }
            
            return creditLimit - totalUsed;
        }
    }
}
