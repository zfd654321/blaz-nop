package test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) 
@ContextConfiguration(locations={"classpath:spring/spring-*.xml","classpath:spring/applicationContext.xml"})
public class BaseJunit4Test {

}
