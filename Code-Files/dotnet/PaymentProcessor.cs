/*
NOTE: This code was generated with AI assistance for training purposes.

This file is part of Atmosera's AI Adoption Training.
It contains intentional code health issues (high complexity, poor naming, 
missing tests, security vulnerabilities, etc.) designed to help learners practice 
establishing code health baselines and tracking meaningful metrics with AI assistance.

Activity: Code Health Observatory - Metrics, Dashboards, and What Actually Matters
Purpose: Payment processing module with measurable quality issues
Focus Area: Complexity metrics, test coverage, security vulnerabilities

Code Health Issues in This File:
- Cyclomatic complexity of 42 (threshold: 10-15)
- Manual thread management increasing complexity
- Deeply nested conditionals (6+ levels)
- Business logic mixed with infrastructure concerns
- Zero unit tests on critical validation logic
- Raw JDBC usage with potential SQL injection risks
- No input validation on payment amounts
- Hard-coded configuration values
- Magic numbers throughout (30, 100, 1000)
- Poor error handling and logging
- No retry logic for transient failures
*/

using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Threading;

namespace CoreBillingEngine
{
    public class PaymentProcessor
    {
        private string connStr = "Server=prod-db;Database=billing;User Id=admin;Password=admin123;";
        private List<Thread> threads = new List<Thread>();

        // Cyclomatic complexity: 42 (way above threshold of 10-15)
        public bool ProcessPayment(string customerId, double amount, string method, Dictionary<string, string> metadata)
        {
            // No input validation
            if (amount > 0)
            {
                if (method == "credit_card")
                {
                    if (metadata.ContainsKey("card_number"))
                    {
                        string cardNum = metadata["card_number"];
                        if (cardNum.Length == 16)
                        {
                            if (cardNum.StartsWith("4") || cardNum.StartsWith("5"))
                            {
                                // Deeply nested - 6 levels deep
                                if (amount < 100)
                                {
                                    return ProcessSmallPayment(customerId, amount, cardNum);
                                }
                                else if (amount < 1000)
                                {
                                    return ProcessMediumPayment(customerId, amount, cardNum);
                                }
                                else
                                {
                                    if (metadata.ContainsKey("authorization_code"))
                                    {
                                        return ProcessLargePayment(customerId, amount, cardNum, metadata["authorization_code"]);
                                    }
                                    else
                                    {
                                        Console.WriteLine("Missing auth code");
                                        return false;
                                    }
                                }
                            }
                            else
                            {
                                Console.WriteLine("Unsupported card type");
                                return false;
                            }
                        }
                        else
                        {
                            Console.WriteLine("Invalid card length");
                            return false;
                        }
                    }
                    else
                    {
                        Console.WriteLine("No card number");
                        return false;
                    }
                }
                else if (method == "bank_transfer")
                {
                    if (metadata.ContainsKey("account_number"))
                    {
                        if (metadata.ContainsKey("routing_number"))
                        {
                            return ProcessBankTransfer(customerId, amount, metadata["account_number"], metadata["routing_number"]);
                        }
                        else
                        {
                            Console.WriteLine("No routing number");
                            return false;
                        }
                    }
                    else
                    {
                        Console.WriteLine("No account number");
                        return false;
                    }
                }
                else if (method == "paypal")
                {
                    if (metadata.ContainsKey("email"))
                    {
                        return ProcessPayPal(customerId, amount, metadata["email"]);
                    }
                    else
                    {
                        Console.WriteLine("No PayPal email");
                        return false;
                    }
                }
                else
                {
                    Console.WriteLine("Unknown payment method");
                    return false;
                }
            }
            else
            {
                Console.WriteLine("Invalid amount");
                return false;
            }
        }

        // SQL Injection vulnerability - using raw SQL with string concatenation
        private bool ProcessSmallPayment(string customerId, double amount, string cardNum)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    // SECURITY ISSUE: SQL Injection vulnerability
                    string sql = "INSERT INTO payments (customer_id, amount, card_last4, status) VALUES ('" 
                        + customerId + "', " + amount + ", '" + cardNum.Substring(12) + "', 'completed')";
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

        // Manual thread management - infrastructure mixed with business logic
        private bool ProcessMediumPayment(string customerId, double amount, string cardNum)
        {
            bool success = false;
            Thread t = new Thread(() =>
            {
                // Simulate async processing
                Thread.Sleep(100);
                success = ProcessSmallPayment(customerId, amount, cardNum);
                
                // Magic number - 30 second timeout
                if (success)
                {
                    Thread.Sleep(30 * 1000);
                    SendConfirmationEmail(customerId, amount);
                }
            });
            
            threads.Add(t);
            t.Start();
            t.Join(5000); // Magic number - 5 second timeout
            
            return success;
        }

        private bool ProcessLargePayment(string customerId, double amount, string cardNum, string authCode)
        {
            // Duplicate validation logic
            if (authCode.Length != 6)
            {
                Console.WriteLine("Invalid auth code");
                return false;
            }

            // More SQL injection
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    string sql = "INSERT INTO payments (customer_id, amount, card_last4, auth_code, status) VALUES ('" 
                        + customerId + "', " + amount + ", '" + cardNum.Substring(12) + "', '" + authCode + "', 'pending_review')";
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    cmd.ExecuteNonQuery();
                }
                
                // No retry logic for transient failures
                NotifyFraudTeam(customerId, amount);
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.Message);
                return false;
            }
        }

        private bool ProcessBankTransfer(string customerId, double amount, string accountNum, string routingNum)
        {
            // Duplicate code pattern from credit card processing
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    string sql = "INSERT INTO payments (customer_id, amount, account_last4, routing, status) VALUES ('" 
                        + customerId + "', " + amount + ", '" + accountNum.Substring(accountNum.Length - 4) + "', '" 
                        + routingNum + "', 'processing')";
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

        private bool ProcessPayPal(string customerId, double amount, string email)
        {
            // More duplicate code
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    string sql = "INSERT INTO payments (customer_id, amount, paypal_email, status) VALUES ('" 
                        + customerId + "', " + amount + ", '" + email + "', 'completed')";
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

        private void SendConfirmationEmail(string customerId, double amount)
        {
            // Stub - no implementation
            Console.WriteLine($"Email sent to {customerId} for ${amount}");
        }

        private void NotifyFraudTeam(string customerId, double amount)
        {
            // Stub - no implementation
            Console.WriteLine($"Fraud team notified about ${amount} payment from {customerId}");
        }

        // No proper cleanup of threads
        ~PaymentProcessor()
        {
            foreach (var t in threads)
            {
                if (t.IsAlive)
                {
                    t.Abort(); // Deprecated and dangerous
                }
            }
        }
    }
}
