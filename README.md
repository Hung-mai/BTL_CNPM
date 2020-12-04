Tên phần mềm: Neighborhood finance management software

Tóm tắt: Phần mềm dùng để quản lý các khoản thu, chi của các hộ dân trong khu dân cư. Tăng khả năng quản lý, tăng tính chính xác và sự tiện lợi cho người quản lý tài chính trong khu dân cư.

Nhóm phát triển: Nhóm 14
- Mai Tuấn Hưng
- Nguyễn Tiến Dũng
- Phan Văn Chương
- Bùi Minh Tuấn

Source code: https://github.com/Hung-mai/BTL_CNPM.git

Công nghệ sử dụng: 
- jdk 1.8.0_261
- jre 1.8.0_261
- xampp 7.2.34
- tomcat 7
- MySQL 
- Công cụ quản lý cơ sở dữ liệu: phpmyadmin 5.0.2
- Giao diện: Java Swing

Các chức năng chính:
- Quản lý hộ dân: 
    + Bao gồm các chức năng list ra danh sách hộ dân
    + Các chức năng thêm, sửa, xóa, thay đổi thông tin của hộ dân
    + Chức năng tìm kiếm theo tên chủ hộ
    + Tính toán ra tổng số hộ dân trong khu dân cư
- Quản lý các khoản thu chi:
    + Chức năng tính toán ra khoản vệ sinh dựa trên số nhân khẩu của từng hộ dân
    + Chức năng list ra danh sách các khoản thu, đóng góp
    + Các chức năng thêm, sửa, xóa, cập nhật các khoản thu
    + Chức năng tìm kiếm khoản thu
    + Chức năng tính toán ra tổng số tiền của từng khoản, từng hộ
    
Cài đặt cơ sở dữ liệu:

    SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
    START TRANSACTION;
    SET time_zone = "+00:00";

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng fee
--

    CREATE TABLE fee (
      fId int(11) NOT NULL,
      name varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
      totalMoney int(11) NOT NULL,
      numberOfHousehold int(11) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng household
--

    CREATE TABLE household (
      hId int(11) NOT NULL,
      householder varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
      numberOfPeople int(11) DEFAULT NULL,
      money int(11) DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------
--
-- Cấu trúc bảng cho bảng listfee
--
    CREATE TABLE listfee (
      hId int(11) DEFAULT NULL,
      fId int(11) DEFAULT NULL,
      money int(11) DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng fee
--
    ALTER TABLE fee
      ADD PRIMARY KEY (fId);

--
-- Chỉ mục cho bảng household
--
    ALTER TABLE household
      ADD PRIMARY KEY (hId);

--
-- Chỉ mục cho bảng listfee
--
    ALTER TABLE listfee
      ADD KEY hId (hId),
      ADD KEY fId (fId);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng fee
--
    ALTER TABLE fee
      MODIFY fId int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT cho bảng household
--
    ALTER TABLE household
      MODIFY hId int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng listfee
--
    ALTER TABLE listfee
      ADD CONSTRAINT listfee_ibfk_1 FOREIGN KEY (hId) REFERENCES household (hId),
      ADD CONSTRAINT listfee_ibfk_2 FOREIGN KEY (fId) REFERENCES fee (fId);
    COMMIT;
