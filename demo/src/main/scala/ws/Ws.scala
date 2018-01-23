package lin.ws



import javax.jws.WebService
import javax.ws.rs._
import javax.ws.rs.core.MediaType


import lin.demo.ws.User
import org.springframework.stereotype.Component

import java.util._;

@WebService
trait IHelloService {
def sayHello (username: String): String
}

@Component
@WebService(serviceName = "helloService2", endpointInterface = "lin.ws.IHelloService")
class HelloService2 extends IHelloService {
  def sayHello(username: String): String = {
    return username + "! Welcome to CXF in Method[HelloService sayHello]"
  }
}

@Path("/heloworld234")
@Produces(Array(MediaType.APPLICATION_JSON))
trait HelloWorld3 {
  @GET
  @Path("/say/{name}") def say(@PathParam("name") name: String): String

  @GET
  @Path("/say1") def say2: String

  @GET
  @Path("/get") def get: User

  @POST
  @Path("/sayHello/{name}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def sayHello(@PathParam("name") name: String, @PathParam("user") user: User): String

  @GET
  @Path("/getList/{id}") def getList(@PathParam("id") id: Long): List[User]
}

@Component
@Path ("/heloworld678")
// (endpointInterface="com.demo.HelloWorld",serviceName="HelloWorld")
class HelloWorldImpl3 extends HelloWorld3 {
  def say (name: String): String = {
  return name + "，您好！"
}

  def sayHello (name: String, user: User): String = {
  return user.getName + "，您好！"
}

  def getList (id: Long): List[User] = {
  val list: List[User] = new ArrayList[User]
  var sid: Long = 1L
  var user: User = new User (sid, "张三" + sid, 21)
  list.add (user)
  sid = 2L
  user = new User (sid, "张三" + sid, 21)
  list.add (user)
  sid = 3L
  user = new User (sid, "张三" + sid, 21)
  list.add (user)
  return list
}

  def say2: String = {
  return "ok."
}

  def get: User = {
  val sid: Long = 123
  val user: User = new User (sid, "张三" + sid, 21)
  return user
}
}