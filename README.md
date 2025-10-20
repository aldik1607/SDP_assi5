# Banking & Investment App

You are designing a **Digital Banking & Investment System**.

## 1. Decorator Pattern (Account Services)

- **Base component:** `Account` interface.  
- **Concrete classes:** `SavingsAccount`, `InvestmentAccount`.  
- **Decorators:**  
  - `InsuranceDecorator` → adds insurance benefits.  
  - `TaxOptimizerDecorator` → optimizes for lower taxes.  
  - `RewardPointsDecorator` → adds loyalty points system.  

**Example:** `SavingsAccount + Insurance + RewardPoints`.

---

## 2. Facade Pattern (BankingFacade)

Customers want high-level operations:

- `openAccountWithBenefits()` → creates account + decorators.  
- `investWithSafetyMode()` → creates investment account with tax optimization and insurance.  
- `closeAccount()` → handles all cleanup.  

**The Facade hides the complexity** of managing decorated accounts and applying multiple rules.

---

## 3. Demo

- Create `SavingsAccount` with `RewardPoints` + `Insurance`.  
- Create `InvestmentAccount` with `TaxOptimizer`.  
- Use `BankingFacade` to simulate customer actions like `deposit`, `invest`, `close account`.
