INSERT INTO genres (`name`) VALUES ('novel'), ('fantasy'), ('poetry');
INSERT INTO authors (`name`, `surname`) VALUES ('Lev', 'Tolstoy'), ('Joseph', 'Brodskiy'), ('John', 'Tolkien');
INSERT INTO books (`title`, `author_id`, `genre_id`) VALUES ('War and Peace', 1, 1), ('Rozhdestvenskie stikhi', 2, 3), ('The Hobbit', 3, 2);
INSERT INTO comments(`content`, `book_id`) VALUES ('The 10 Greatest Books of All Time', 1), ('Story about hobbit Bilbo Baggins', 3), ('Nominated for the Carnegie Medal and awarded a prize from the New York Herald Tribune for best juvenile fiction', 3);


