package com.datasource.multiconnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.datasource.multiconnect.model.Employee;
import com.datasource.multiconnect.model.Student;
import com.datasource.multiconnect.repository.EmployeeRepository;
import com.datasource.multiconnect.repository.StudentRepository;
import com.datasource.multiconnect.service.MultipleDaoService;


@SpringBootTest
class MulticonnectApplicationTests {

	@Mock
	ApplicationContext apc;
	
	@Mock
	EmployeeRepository empmapper;
	
	@Mock
	StudentRepository stmapper;
	
	@Mock
	SqlSessionTemplate sst;
	
	@InjectMocks
	MultipleDaoService multipleDaoService;

	@Test
	public void test_getAllEmployees() {
		
		List<Employee> empList=new ArrayList<>();
		
		Employee emp1=new Employee(5,"amit","thakur","akt123@gmail.com");
		Employee emp2=new Employee(6,"amit","thakur","akt123@gmail.com");
		Employee emp3=new Employee(8,"amit","thakur","akt123@gmail.com");
		Employee emp4=new Employee(10,"amit","thakur","akt123@gmail.com");
		
		empList.add(emp1);
		empList.add(emp2);
		empList.add(emp3);
		empList.add(emp4);
		
		when(apc.getBean("sqlSessionTemplatePrimary")).thenReturn(sst);
		when(sst.getMapper(EmployeeRepository.class)).thenReturn(empmapper);
		when(empmapper.findAll()).thenReturn(empList);
		
		List<Employee> empList2=multipleDaoService.getAllEmployees("Primary");
		
		assertEquals(4, empList2.size());
		assertEquals(empList.size(), empList2.size());

	}
	
	@Test
	public void test_getAllStudents() {
		List<Student> stlist=new ArrayList<>();
		Student s1=new Student(10,"rakesh");
		Student s2=new Student(11,"anita");
		
		stlist.add(s1);
		stlist.add(s2);
		
		when(apc.getBean("sqlSessionTemplateSecondary")).thenReturn(sst);
		when(sst.getMapper(StudentRepository.class)).thenReturn(stmapper);
		when(stmapper.findAllStudents()).thenReturn(stlist);
		
		List<Student> stlist2=multipleDaoService.getAllStudents("Secondary");
		
		assertEquals(2, stlist2.size());
		assertEquals(stlist.size(), stlist2.size());
	}

}
