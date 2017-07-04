package github.and777;

import github.and777.protobuf.AddressBookProtos.AddressBook;
import github.and777.protobuf.AddressBookProtos.Person;
import github.and777.util.ProtoCompile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author edliao on 2017/7/4.
 * @description 测试序列化
 */
public class Application {

  public static void send() throws IOException {
    Person john =
        Person.newBuilder()
            .setId(1234)
            .setName("John Doe")
            .setEmail("jdoe@example.com")
            .addPhones(
                Person.PhoneNumber.newBuilder()
                    .setNumber("555-4321")
                    .setType(Person.PhoneType.HOME))
            .build();

    FileOutputStream output = new FileOutputStream(ProtoCompile.masterProjectPath + "/mark/db.txt");
    AddressBook.newBuilder().addPeople(john).build().writeTo(output);
    output.close();
  }

  public static void read() throws IOException {
    FileInputStream input = new FileInputStream(ProtoCompile.masterProjectPath + "/mark/db.txt");
    AddressBook addressBook =
        AddressBook.parseFrom(input);
    addressBook.getPeopleList().forEach(person -> System.out.println(person.getEmail()));
  }

  public static void main(String[] a) throws IOException {
    send();
    read();
  }
}
