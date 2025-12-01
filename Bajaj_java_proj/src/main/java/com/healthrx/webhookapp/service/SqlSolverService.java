// package com.healthrx.webhookapp.service;

// import org.springframework.stereotype.Service;

// @Service
// public class SqlSolverService {

//     public String buildFinalQuery() {
//         return "WITH filtered_payments AS ( " +
//                 "SELECT p.EMP_ID, MAX(p.AMOUNT) AS SALARY " +
//                 "FROM PAYMENTS p " +
//                 "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
//                 "GROUP BY p.EMP_ID ), " +
//                 "ranked_employees AS ( " +
//                 "SELECT d.DEPARTMENT_NAME, fp.SALARY, " +
//                 "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME, " +
//                 "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
//                 "ROW_NUMBER() OVER (PARTITION BY d.DEPARTMENT_ID ORDER BY fp.SALARY DESC) AS rn, " +
//                 "d.DEPARTMENT_ID " +
//                 "FROM EMPLOYEE e " +
//                 "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
//                 "JOIN filtered_payments fp ON e.EMP_ID = fp.EMP_ID " +
//                 ") " +
//                 "SELECT DEPARTMENT_NAME, SALARY, EMPLOYEE_NAME, AGE " +
//                 "FROM ranked_employees " +
//                 "WHERE rn = 1 " +
//                 "ORDER BY DEPARTMENT_ID DESC;";
//     }
// }

package com.healthrx.webhookapp.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolverService {

    public String buildFinalQuery() {
        return "WITH filtered_payments AS (" +
                "    SELECT p.EMP_ID, MAX(p.AMOUNT) AS SALARY " +
                "    FROM PAYMENTS p " +
                "    WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                "    GROUP BY p.EMP_ID" +
                "), ranked_employees AS (" +
                "    SELECT d.DEPARTMENT_NAME, fp.SALARY, " +
                "           CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME, " +
                "           TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                "           ROW_NUMBER() OVER (PARTITION BY d.DEPARTMENT_ID ORDER BY fp.SALARY DESC) AS rn, " +
                "           d.DEPARTMENT_ID " +
                "    FROM EMPLOYEE e " +
                "    JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "    JOIN filtered_payments fp ON e.EMP_ID = fp.EMP_ID " +
                ") " +
                "SELECT DEPARTMENT_NAME, SALARY, EMPLOYEE_NAME, AGE " +
                "FROM ranked_employees " +
                "WHERE rn = 1 " +
                "ORDER BY DEPARTMENT_ID DESC;";
    }
}
