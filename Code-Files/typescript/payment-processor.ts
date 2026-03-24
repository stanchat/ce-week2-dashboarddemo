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
- Deeply nested conditionals (6+ levels)
- Business logic mixed with infrastructure concerns
- Zero unit tests on critical validation logic
- SQL injection vulnerabilities in database operations
- No input validation on payment amounts
- Hard-coded configuration values
- Magic numbers throughout (30, 100, 1000)
- Poor error handling and logging
- No retry logic for transient failures
- Missing TypeScript strict mode
- Any types used throughout
*/

interface PaymentMetadata {
    [key: string]: string;
}

class PaymentProcessor {
    // Hard-coded credentials - security issue
    private connStr: string = "postgresql://admin:admin123@prod-db:5432/billing";
    private threads: any[] = [];

    // Cyclomatic complexity: 42 (way above threshold of 10-15)
    processPayment(customerId: string, amount: number, method: string, metadata: PaymentMetadata): boolean {
        // No input validation
        if (amount > 0) {
            if (method === "credit_card") {
                if (metadata["card_number"]) {
                    const cardNum = metadata["card_number"];
                    if (cardNum.length === 16) {
                        if (cardNum.startsWith("4") || cardNum.startsWith("5")) {
                            // Deeply nested - 6 levels deep
                            if (amount < 100) {
                                return this.processSmallPayment(customerId, amount, cardNum);
                            } else if (amount < 1000) {
                                return this.processMediumPayment(customerId, amount, cardNum);
                            } else {
                                if (metadata["authorization_code"]) {
                                    return this.processLargePayment(
                                        customerId, amount, cardNum, 
                                        metadata["authorization_code"]);
                                } else {
                                    console.log("Missing auth code");
                                    return false;
                                }
                            }
                        } else {
                            console.log("Unsupported card type");
                            return false;
                        }
                    } else {
                        console.log("Invalid card length");
                        return false;
                    }
                } else {
                    console.log("No card number");
                    return false;
                }
            } else if (method === "bank_transfer") {
                if (metadata["account_number"]) {
                    if (metadata["routing_number"]) {
                        return this.processBankTransfer(
                            customerId, amount, 
                            metadata["account_number"], 
                            metadata["routing_number"]);
                    } else {
                        console.log("No routing number");
                        return false;
                    }
                } else {
                    console.log("No account number");
                    return false;
                }
            } else if (method === "paypal") {
                if (metadata["email"]) {
                    return this.processPayPal(customerId, amount, metadata["email"]);
                } else {
                    console.log("No PayPal email");
                    return false;
                }
            } else {
                console.log("Unknown payment method");
                return false;
            }
        } else {
            console.log("Invalid amount");
            return false;
        }
    }

    // SQL Injection vulnerability - using template strings without sanitization
    private processSmallPayment(customerId: string, amount: number, cardNum: string): boolean {
        try {
            // CRITICAL: SQL Injection vulnerability (simulated)
            const sql = `INSERT INTO payments (customer_id, amount, card_last4, status) VALUES ('${customerId}', ${amount}, '${cardNum.substring(12)}', 'completed')`;
            
            // Simulated database call
            console.log("Executing: " + sql);
            return true;
        } catch (ex: any) {
            console.log("Error: " + ex.message);
            return false;
        }
    }

    // Async handling without proper error management
    private processMediumPayment(customerId: string, amount: number, cardNum: string): boolean {
        let success = false;

        // Magic number - 30 second delay
        setTimeout(() => {
            success = this.processSmallPayment(customerId, amount, cardNum);
            if (success) {
                setTimeout(() => {
                    this.sendConfirmationEmail(customerId, amount);
                }, 30 * 1000);
            }
        }, 100);

        return success;
    }

    private processLargePayment(customerId: string, amount: number, cardNum: string, authCode: string): boolean {
        // Duplicate validation logic
        if (authCode.length !== 6) {
            console.log("Invalid auth code");
            return false;
        }

        // More SQL injection
        try {
            // CRITICAL: SQL Injection vulnerability
            const sql = `INSERT INTO payments (customer_id, amount, card_last4, auth_code, status) VALUES ('${customerId}', ${amount}, '${cardNum.substring(12)}', '${authCode}', 'pending_review')`;
            
            console.log("Executing: " + sql);
            
            // No retry logic for transient failures
            this.notifyFraudTeam(customerId, amount);
            return true;
        } catch (ex: any) {
            console.log("Error: " + ex.message);
            return false;
        }
    }

    private processBankTransfer(customerId: string, amount: number, accountNum: string, routingNum: string): boolean {
        // Duplicate code pattern from credit card processing
        try {
            // CRITICAL: SQL Injection vulnerability
            const sql = `INSERT INTO payments (customer_id, amount, account_last4, routing, status) VALUES ('${customerId}', ${amount}, '${accountNum.substring(accountNum.length - 4)}', '${routingNum}', 'processing')`;
            
            console.log("Executing: " + sql);
            return true;
        } catch (ex: any) {
            console.log("Error: " + ex.message);
            return false;
        }
    }

    private processPayPal(customerId: string, amount: number, email: string): boolean {
        // More duplicate code
        try {
            // CRITICAL: SQL Injection vulnerability
            const sql = `INSERT INTO payments (customer_id, amount, paypal_email, status) VALUES ('${customerId}', ${amount}, '${email}', 'completed')`;
            
            console.log("Executing: " + sql);
            return true;
        } catch (ex: any) {
            console.log("Error: " + ex.message);
            return false;
        }
    }

    private sendConfirmationEmail(customerId: string, amount: number): void {
        // Stub - no implementation
        console.log(`Email sent to ${customerId} for $${amount}`);
    }

    private notifyFraudTeam(customerId: string, amount: number): void {
        // Stub - no implementation
        console.log(`Fraud team notified about $${amount} payment from ${customerId}`);
    }
}

export default PaymentProcessor;
