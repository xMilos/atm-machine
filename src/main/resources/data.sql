CREATE TABLE account (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  account_number VARCHAR(250) NOT NULL,
  pin INT NOT NULL,
  balance INT NOT NULL,
  overdraft INT NOT NULL
);

INSERT INTO account (account_number, pin, balance,overdraft) VALUES
  ('123456789', 1234, 800,200),
  ('987654321', 4321, 1230,150);