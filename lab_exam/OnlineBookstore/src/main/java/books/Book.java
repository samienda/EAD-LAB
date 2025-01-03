package books;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
// SAMUEL ENDALE UGR/9314/14

@Getter
@Setter
@AllArgsConstructor
public class Book {
    public int id;
    public String title;
    public String author;
    public Double price;
}
