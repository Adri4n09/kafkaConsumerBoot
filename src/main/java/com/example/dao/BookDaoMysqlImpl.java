package com.example.dao;

import com.example.model.Book;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@Profile("mysql")
public class BookDaoMysqlImpl implements BookDao {

    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public Book getBookByISBN(String isbn) {
        List<Book> list = sessionFactory.getCurrentSession().createQuery("from Book where isbn = ?")
                .setParameter(0, isbn).list();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> getAllBooks() {
        List<Book> list = sessionFactory.getCurrentSession().createQuery("from Book").list();
        return list;
    }

    @Override
    @Transactional
    public void addBook(Book book) {
        sessionFactory.getCurrentSession().save(book);
    }

    @Override
    public void deleteBook(Book book) {
        sessionFactory.getCurrentSession().delete(book);
    }

    @Override
    public void updateBook(Book book) {
        sessionFactory.getCurrentSession().update(book);
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
