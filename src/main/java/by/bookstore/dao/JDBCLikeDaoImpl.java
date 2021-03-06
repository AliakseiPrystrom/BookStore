package by.bookstore.dao;

import by.bookstore.entity.Book;
import by.bookstore.entity.Like;
import by.bookstore.entity.TypeOfUser;
import by.bookstore.entity.User;
import by.bookstore.utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCLikeDaoImpl implements LikeDAO {
    private static final String GET_LIKES_BY_BOOK = " SELECT likes.id AS like_id, u.id AS id_user, u.name AS name_user, u.typeOfUser, u.picture, b.id AS id_book, b.name AS name_book, b.author" +
            " FROM likes LEFT JOIN books b on b.id = likes.book_id LEFT JOIN users u on u.id = likes.user_id WHERE book_id = ? ";
    private static final String SAVE = " INSERT INTO likes (user_id, book_id) VALUES (?, ?) ";
    private static final String IS_EXIST_BY_USER_BOOK = " SELECT * FROM likes WHERE user_id = ? and book_id = ? ";

    //for methods: save, isExistByUserAndBook
    private static final int USER_ID = 1;
    private static final int BOOK_ID = 2;

    //for methods: getLikesByBook
    private static final int BOOK_ID2 = 1;

    @Override
    public boolean save(Like like, Connection connection) {
        try  {
            PreparedStatement statement = connection.prepareStatement(SAVE);
            statement.setLong(USER_ID, like.getUser().getId());
            statement.setLong(BOOK_ID, like.getBook().getId());
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isExistByUserAndBook(Like like, Connection connection) {
        try  {
            PreparedStatement statement = connection.prepareStatement(IS_EXIST_BY_USER_BOOK);
            statement.setLong(USER_ID, like.getUser().getId());
            statement.setLong(BOOK_ID, like.getBook().getId());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Like> getLikesByBook(long idBook, Connection connection) {
        List<Like> likeList = new ArrayList<>();
        try  {
            PreparedStatement statement = connection.prepareStatement(GET_LIKES_BY_BOOK);
            statement.setLong(BOOK_ID2, idBook);
            ResultSet resultSet = statement.executeQuery();
            likeList = getLikesFromResult(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return likeList;
    }

    private List<Like> getLikesFromResult(ResultSet resultSet) throws SQLException {
        List<Like> likeList = new ArrayList<>();
        while (resultSet.next()) {
            likeList.add(new Like(
                    resultSet.getLong("like_id"),
                    new User(
                            resultSet.getLong("id_user"),
                            resultSet.getString("name_user"),
                            resultSet.getString("picture"),
                            TypeOfUser.valueOf(resultSet.getString("typeOfUser"))
                    ),
                    new Book(
                            resultSet.getLong("id_book"),
                            resultSet.getString("name_book"),
                            resultSet.getString("author")
                    )));
        }
        return likeList;
    }
}
