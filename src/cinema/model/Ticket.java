package cinema.model;

public class Ticket {

    private int id;
    private User user;  //Если место не куплено, тогда значение пользователя должно быть пустым.
    private Movie movie;
    private int seatNum;
    private double price;
    private boolean isNotBought;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean getIsNotBought() {
        return isNotBought;
    }

    public void setIsNotBought(boolean isNotBought) {
        this.isNotBought = isNotBought;
    }

    public Ticket(User user, Movie movie, int seatNum, double price, boolean isNotBought) {
        this.user = user;
        this.movie = movie;
        this.seatNum = seatNum;
        this.price = price;
        this.isNotBought = isNotBought;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "user=" + user +
                ", movie=" + movie +
                ", seatNum=" + seatNum +
                ", price=" + price +
                ", isNotBought=" + isNotBought +
                '}';
    }
}
