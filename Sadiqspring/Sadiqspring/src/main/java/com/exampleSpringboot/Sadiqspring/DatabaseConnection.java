package com.exampleSpringboot.Sadiqspring;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class DatabaseConnection {
	
	

	Connection connection=null;
	
	public Connection getConnection() throws SQLException {
		if(this.connection!=null)
			return this.connection;
		else
			return connection=dataSource.getConnection();
	}
	
	
	
	
	@Autowired
	DataSource dataSource;
	
	 //  private static String instemp="insert into employees values((select max(employee_id)+1 from employees),"
	    //		+ "FIRST_NAME,LAST_NAME,EMAIL,PHONE,HIRE_DATE,10,JOB_TITLE,SALARY)";
	    
    
    private static final String udpemployee ="update employees set FIRST_NAME=?,last_name=? where employee_id=?";
    private static final String deleteEmployee ="delete from employees where email=?";
    private static final String insertfile="insert into employee_cv values((select max(fileid)+1 from employee_cv),?,?,?,?,?)";

    
    public Employee_CV getCV(int employee_id) {
    	
    	Employee_CV emp =new Employee_CV();
		
		try {
            
			Statement stmt = dataSource.getConnection().createStatement();
		ResultSet rs =stmt.executeQuery("select * from employee_cv where employee_id="+employee_id);			
			while(rs.next()) {
				emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
				emp.setContent_type(rs.getString("CONTENT_TYPE"));
				emp.setContent(rs.getBytes("FILE_CONTENT"));
				emp.setFile_size(rs.getLong("FILE_SIZE"));
				emp.setFileId(rs.getInt("FILEID"));
				emp.setFileName(rs.getString("FILE_NAME"));    
				
				
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
return emp;
    	
    }
    
    
    // insert  cv
    
    public int insertEmployeeCV(Employee_CV cv) throws IOException {
    	int result=0;
    	PreparedStatement stmt =null;
    	try {  			
    			 stmt =  dataSource.getConnection().prepareStatement(insertfile);
    			stmt.setBytes(1,cv.getFile_content().getBytes());
    			stmt.setInt(2, cv.getEmployee_id());
    			stmt.setString(3, cv.getFileName());
    			stmt.setLong(4, cv.getFile_size());
    			stmt.setString(5, cv.getContent_type());
    			result =  stmt.executeUpdate();
    				
    	
    				
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}finally {
    		try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    			return result;
    	
    	
    }
    
    
    // multiple insert
    
    
public int insertEmployees(List<Employee> emps) {
    	
    	int result=0;
try {
	for(Employee emp:emps) {
		String instemp="insert into employees values((select max(employee_id)+1 from employees),"
	    		+ "FIRST_NAME,LAST_NAME,EMAIL,PHONE,HIRE_DATE,10,JOB_TITLE,SALARY)";
	    
		instemp=	instemp.replace("FIRST_NAME", "'"+emp.getFirst_name()+"'")
				.replace("LAST_NAME", "'"+emp.getLast_name()+"'")
				.replace("EMAIL", "'"+emp.getEmail()+"'")
				.replace("PHONE", "'"+emp.getPhone()+"'")
				.replace("HIRE_DATE","sysdate")
				.replace("JOB_TITLE", "'"+emp.getJob_title()+"'").replace("SALARY",Long.toString(emp.getSalary()));
		
		System.out.println(instemp);
		Statement stmt =  dataSource.getConnection().createStatement();
		result +=  stmt.executeUpdate(instemp);
			
	}
			
} catch (SQLException e) {
	e.printStackTrace();
}	
		return result;
    	
    }


// insert employee
    
    
    public int insertEmployee(Employee emp) {
    	
    	int result=0;
try {
	String instemp="insert into employees values((select max(employee_id)+1 from employees),"
    		+ "FIRST_NAME,LAST_NAME,EMAIL,PHONE,HIRE_DATE,10,JOB_TITLE,SALARY)";
    
	
	if(emp!=null) {
		instemp =instemp.replace("FIRST_NAME", "'"+emp.getFirst_name()+"'")
		.replace("LAST_NAME", "'"+emp.getLast_name()+"'")
		.replace("EMAIL", "'"+emp.getEmail()+"'")
		.replace("PHONE", "'"+emp.getPhone()+"'")
		.replace("HIRE_DATE","sysdate")
		.replace("JOB_TITLE", "'"+emp.getJob_title()+"'").replace("SALARY",Long.toString(emp.getSalary()));
		System.out.println(instemp);
		
			Statement stmt =  dataSource.getConnection().createStatement();
		result =stmt.executeUpdate(instemp);
			
	}
			
} catch (SQLException e) {
	e.printStackTrace();
}	
		return result;
    	
    }
    
    
    
    // update 
    
    public int updateEmployee(Employee emp) {
    	int result=0;
try {
            //update employees set first_name=? where empl_id=?
			PreparedStatement preparestmt =  dataSource.getConnection().prepareStatement(udpemployee);
			//
			preparestmt.setString(1, emp.getFirst_name());
			preparestmt.setString(2, emp.getLast_name());
			//	update employees set first_name='microcare' where empl_id=?
			preparestmt.setInt(3, emp.getEmployee_id());
//				update employees set first_name='microcare' where empl_id=353523
				 result =preparestmt.executeUpdate();
			
			
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
return result;
    	
    	
    	
    }
    
    
    // delete employee 
    
    
    public int deleteEmployee(String email) {
    	int result=0;
    	try {
    				PreparedStatement preparestmt =  dataSource.getConnection().prepareStatement(deleteEmployee);
    				preparestmt.setString(1, email);
    			    result =preparestmt.executeUpdate();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
    	return result;
    	    	
    	
    	
    }
    
    
    // fetch multiple employees cv on browser
    
    public List<Employee> getEmployees(){
    	List<Employee> employees = new ArrayList<Employee>();
          Employee emp;
          Statement stmt1 =null;
          Statement stmt = null;
		try {
			 stmt1 = dataSource.getConnection().createStatement();
			 stmt = dataSource.getConnection().createStatement();
		ResultSet rs =stmt.executeQuery("select * from employees");			
			while(rs.next()) {
				emp=new Employee();
				emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
				emp.setFirst_name(rs.getString("FIRST_NAME"));
				emp.setLast_name(rs.getString("LAST_NAME"));
				emp.setPhone(rs.getString("PHONE"));
				emp.setEmail(rs.getString("EMAIL"));
				emp.setJob_title(rs.getString("JOB_TITLE"));
				emp.setHire_date(rs.getDate("HIRE_DATE").toLocalDate());
				emp.setManager_id(rs.getInt("MANAGER_ID"));
				
				ResultSet rs1 =stmt1.executeQuery("select * from employee_cv where employee_id="+rs.getInt("EMPLOYEE_ID"));	
				while(rs1.next()) {
				emp.setCv(true);
				}
				employees.add(emp);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				stmt.close();
				stmt1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
return employees;
    	
    	
    }
    
    
    
    // fetch employees on browser
    
    
	public  Employee getEmployee(int emp_id) {
		
		Employee emp =new Employee();
		
		try {
            
			Statement stmt = dataSource.getConnection().createStatement();
		ResultSet rs =stmt.executeQuery("select * from employees where employee_id="+emp_id);			
			while(rs.next()) {
				emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
				emp.setFirst_name(rs.getString("FIRST_NAME"));
				emp.setLast_name(rs.getString("LAST_NAME"));
				emp.setPhone(rs.getString("PHONE"));
				emp.setEmail(rs.getString("EMAIL"));
				emp.setJob_title(rs.getString("JOB_TITLE"));
				emp.setHire_date(rs.getDate("HIRE_DATE").toLocalDate());
				emp.setManager_id(rs.getInt("MANAGER_ID"));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
return emp;
	}
	
	
	
	
	// fetch employee
	
public  Employee getEmployee(String email) {
		
		Employee emp =new Employee();
		
		try {
            
			Statement stmt = dataSource.getConnection().createStatement();
		ResultSet rs =stmt.executeQuery("select * from employees where email="+email);			
			while(rs.next()) {
				emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
				emp.setFirst_name(rs.getString("FIRST_NAME"));
				emp.setLast_name(rs.getString("LAST_NAME"));
				emp.setPhone(rs.getString("PHONE"));
				emp.setEmail(rs.getString("EMAIL"));
				emp.setJob_title(rs.getString("JOB_TITLE"));
				emp.setHire_date(rs.getDate("HIRE_DATE").toLocalDate());
				emp.setManager_id(rs.getInt("MANAGER_ID"));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
return emp;
	}


// login 

public  Login getUser(String username) {
	
	Login user =new Login();
	
	try {
        
		Statement stmt = dataSource.getConnection().createStatement();
	ResultSet rs =stmt.executeQuery("select * from login where user_name='"+username+"'");			
		while(rs.next()) {
			user.setUser_name(rs.getString("user_name"));
			user.setPassword(rs.getString("password"));
		}
	
	} catch (SQLException e) {
		e.printStackTrace();
	}
return user;
}



	
	// React JSON




public List<Employee> getJsonEmployees(){
List<Employee> employees = new ArrayList<Employee>();
  Employee emp;
  Statement stmt = null;
try {

	 stmt = getConnection().createStatement();
ResultSet rs =stmt.executeQuery("select * from employees");	
System.out.println(rs.getFetchSize());
	while(rs.next()) {
		System.out.println("");
		emp=new Employee();
		emp.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
		emp.setFirst_name(rs.getString("FIRST_NAME"));
		emp.setLast_name(rs.getString("LAST_NAME"));
		emp.setPhone(rs.getString("PHONE"));
		emp.setEmail(rs.getString("EMAIL"));
		emp.setJob_title(rs.getString("JOB_TITLE"));
		emp.setHire_date(rs.getDate("HIRE_DATE").toLocalDate());
		emp.setManager_id(rs.getInt("MANAGER_ID"));
		employees.add(emp);
	}

} catch (SQLException e) {
	e.printStackTrace();
}finally {
	try {
		stmt.close();
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
return employees;


}





	
}