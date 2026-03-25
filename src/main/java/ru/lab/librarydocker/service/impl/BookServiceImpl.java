package ru.lab.librarydocker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lab.librarydocker.dto.request.BookCreateRequest;
import ru.lab.librarydocker.dto.request.BookUpdateRequest;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.entity.Book;
import ru.lab.librarydocker.exception.ResourceNotFoundException;
import ru.lab.librarydocker.mapper.BookDtoMapper;
import ru.lab.librarydocker.repository.AuthorRepository;
import ru.lab.librarydocker.repository.BookRepository;
import ru.lab.librarydocker.service.BookService;
import ru.lab.librarydocker.utils.ValidationUtils;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAllWithAuthorNames();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        return bookRepository.findByIdWithAuthorName(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public BookResponse createBook(BookCreateRequest request) {
        ValidationUtils.validateIsbn(request.getIsbn());
        if (!authorRepository.existsById(request.getAuthorId())) {
            throw new ResourceNotFoundException("Author not found with id: " + request.getAuthorId());
        }
        Book book = BookDtoMapper.toEntity(request);
        Book saved = bookRepository.save(book);
        return bookRepository.findByIdWithAuthorName(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found after creation"));
    }

    @Override
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        if (!authorRepository.existsById(request.getAuthorId())) {
            throw new ResourceNotFoundException("Author not found with id: " + request.getAuthorId());
        }
        Book book = BookDtoMapper.toEntity(request, id);
        Book updated = bookRepository.save(book);
        return bookRepository.findByIdWithAuthorName(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found after update"));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorIdWithAuthorName(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByGenre(String genre) {
        return bookRepository.findByGenreWithAuthorName(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksByAuthorAndGenre(Long authorId, String genre) {
        return bookRepository.findByAuthorIdAndGenreWithAuthorName(authorId, genre);
    }
}
