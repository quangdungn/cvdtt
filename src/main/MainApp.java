package main;

import service.*;
import model.orders.*;
import model.users.*;
import model.products.*;
import model.payments.*;
import dao.*;
import strategy.*;
import adapter.*;
import java.util.*;

public class MainApp {

    public static void main(String[] args) {
        boolean ok = true;
        User loggedInUser = LoginSystem.login();  // Đăng nhập hệ thống
        if (loggedInUser == null) {
            System.out.println("Vui lòng nhập lại.");
        }
        else {ok = false;}  // Nếu đăng nhập thành công thì tiếp tục
        while (ok) {
            loggedInUser = LoginSystem.login();
            if (loggedInUser == null) {
                System.out.println("Vui lòng nhập lại.");
            }
            else break;  // Nếu đăng nhập thành công thì thoát khỏi vòng lặp
        }

        System.out.println("Đã đăng nhập với quyền: " + loggedInUser.getRole());

        Scanner scanner = new Scanner(System.in);

        // Xử lý theo vai trò
        if (loggedInUser.getRole().equals("Staff")) {
            handleStaff(scanner, loggedInUser);  // Staff sẽ quản lý đơn hàng và sản phẩm
        } else if (loggedInUser.getRole().equals("Admin")) {
            handleAdmin(scanner, loggedInUser);  // Admin có quyền quản lý người dùng, khách hàng, đơn hàng và sản phẩm
        }
    }

    // Hàm xử lý khi người dùng là Staff
    private static void handleStaff(Scanner scanner, User loggedInUser) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Menu Staff ---");
            System.out.println("1. Quản lý đơn hàng");
            System.out.println("2. Quản lý sản phẩm");
            System.out.println("3. Thoát");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    manageOrders(scanner, loggedInUser);  // Quản lý đơn hàng
                    break;
                case 2:
                    manageProducts(scanner);  // Quản lý sản phẩm
                    break;
                case 3:
                    exit = true;  // Thoát
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    // Hàm xử lý khi người dùng là Admin
    private static void handleAdmin(Scanner scanner, User loggedInUser) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Menu Admin ---");
            System.out.println("1. Quản lý người dùng");
            System.out.println("2. Quản lý khách hàng");
            System.out.println("3. Quản lý đơn hàng");
            System.out.println("4. Quản lý sản phẩm");
            System.out.println("5. Thoát");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    manageUsers(scanner);  // Quản lý người dùng
                    break;
                case 2:
                    manageCustomers(scanner);  // Quản lý khách hàng
                    break;
                case 3:
                    manageOrders(scanner, loggedInUser);  // Quản lý đơn hàng
                    break;
                case 4:
                    manageProducts(scanner);  // Quản lý sản phẩm
                    break;
                case 5:
                    exit = true;  // Thoát
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void manageOrders(Scanner scanner, User loggedInUser) {
        System.out.println("\n--- Quản lý đơn hàng ---");
        System.out.println("1. Tạo đơn hàng");
        System.out.println("2. Xem danh sách đơn hàng");
        System.out.println("3. Thoát");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                createOrder(scanner, loggedInUser);  // Tạo đơn hàng
                break;
            case 2:
                viewOrders();  // Xem danh sách đơn hàng
                break;
            case 3:
                return;  // Quay lại menu
            default:
                System.out.println("Lựa chọn không hợp lệ.");
        }
    }

    // Tạo đơn hàng, bao gồm lựa chọn sản phẩm và phương thức thanh toán
    private static void createOrder(Scanner scanner, User loggedInUser) {
        System.out.println("Nhập thông tin đơn hàng:");

        // Sử dụng ProductDAO để hiển thị sản phẩm và lựa chọn
        ProductDAO productDAO = new ProductDAO();
        System.out.println("Danh sách sản phẩm:");
        List<Product> products = productDAO.getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.print((i + 1) + ". ");
            products.get(i).displayProductDetails();  // Hiển thị thông tin chi tiết sản phẩm
        }

        List<Product> orderProducts = new ArrayList<>();
        while (true) {
            System.out.print("Nhập số sản phẩm bạn muốn thêm vào đơn hàng (hoặc nhập 0 để kết thúc): ");
            int productChoice = scanner.nextInt();
            if (productChoice == 0) {
                break;  // Kết thúc việc chọn sản phẩm
            } else if (productChoice > 0 && productChoice <= products.size()) {
                Product selectedProduct = products.get(productChoice - 1);
                orderProducts.add(selectedProduct);
                System.out.println("Đã thêm sản phẩm: " + selectedProduct.getName());
            } else {
                System.out.println("Lựa chọn không hợp lệ.");
            }
        }

        double totalAmount = 0;
        System.out.println("\nSản phẩm trong đơn hàng:");
        for (Product product : orderProducts) {
            System.out.println(product.getName() + " - Giá: " + product.getPrice() + " VND");
            totalAmount += product.getPrice();
        }
        System.out.println("Tổng số tiền: " + totalAmount + " VND");

        System.out.println("Chọn phương thức thanh toán:");
        System.out.println("1. Chuyển khoản ngân hàng");
        System.out.println("2. Thẻ tín dụng");
        System.out.println("3. PayPal");
        System.out.println("4. Stripe");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine();

        PaymentStrategy paymentStrategy = null;
        switch (paymentChoice) {
            case 1:
                paymentStrategy = new BankTransferPayment();
                break;
            case 2:
                paymentStrategy = new CreditCardPayment();
                break;
            case 3:
                paymentStrategy = new PayPalPaymentAdapter(new PayPalPayment());
                break;
            case 4:
                paymentStrategy = new StripePaymentAdapter(new StripePayment());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ, mặc định chọn 'Chuyển khoản ngân hàng'");
                paymentStrategy = new BankTransferPayment();
                break;
        }

        paymentStrategy.pay(totalAmount);
        System.out.println("Đơn hàng đã được tạo thành công.");
    }

    private static void viewOrders() {
        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào.");
        } else {
            for (Order order : orders) {
                order.displayOrderDetails();
            }
        }
    }

    private static void manageProducts(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Quản lý sản phẩm ---");
            System.out.println("1. Thêm sản phẩm");
            System.out.println("2. Sửa sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Xem danh sách sản phẩm");
            System.out.println("5. Thoát");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addProduct(scanner);  // Thêm sản phẩm
                    break;
                case 2:
                    updateProduct(scanner);  // Sửa sản phẩm
                    break;
                case 3:
                    deleteProduct(scanner);  // Xóa sản phẩm
                    break;
                case 4:
                    viewProducts();  // Xem danh sách sản phẩm
                    break;
                case 5:
                    exit = true;  // Thoát
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }


    // Thêm sản phẩm
    private static void addProduct(Scanner scanner) {
        System.out.println("Chọn loại sản phẩm:");
        System.out.println("1. Điện tử");
        System.out.println("2. Quần áo");

        int categoryChoice = scanner.nextInt();
        Product product = null;

        if (categoryChoice == 1) {
            // Thêm sản phẩm điện tử
            scanner.nextLine();  // Đọc ký tự newline còn sót lại
            System.out.print("Nhập tên sản phẩm: ");
            String productName = scanner.nextLine();  // Đảm bảo productName là kiểu String
            System.out.print("Nhập giá sản phẩm: ");
            double price = scanner.nextDouble();
            scanner.nextLine(); // Đảm bảo price là kiểu double
            System.out.print("Nhập số lượng: ");
            int stockQuantity = scanner.nextInt();  // Đảm bảo stockQuantity là kiểu int
            scanner.nextLine();  // Đọc ký tự newline còn sót lại
            System.out.print("Nhập mô tả: ");
            String description = scanner.nextLine();  // Đảm bảo description là kiểu String
            System.out.print("Nhập thương hiệu: ");
            String brand = scanner.nextLine();  // Đảm bảo brand là kiểu String

            product = new Electronics(productName, price, description, stockQuantity, brand);  // Tạo đối tượng Electronics
        } else if (categoryChoice == 2) {
            // Thêm sản phẩm quần áo
            scanner.nextLine();  // Đọc ký tự newline còn sót lại
            System.out.print("Nhập tên sản phẩm: ");
            String productName = scanner.nextLine();  // Đảm bảo productName là kiểu String
            System.out.print("Nhập giá sản phẩm: ");
            double price = scanner.nextDouble();  // Đảm bảo price là kiểu double
            System.out.print("Nhập số lượng: ");
            int stockQuantity = scanner.nextInt();  // Đảm bảo stockQuantity là kiểu int
            scanner.nextLine();  // Đọc ký tự newline còn sót lại
            System.out.print("Nhập mô tả: ");
            String description = scanner.nextLine();  // Đảm bảo description là kiểu String
            System.out.print("Nhập size: ");
            String size = scanner.nextLine();  // Đảm bảo size là kiểu String

            product = new Clothing(productName, price, description, stockQuantity, size);  // Tạo đối tượng Clothing
        } else {
            System.out.println("Lựa chọn không hợp lệ.");
        }

        if (product != null) {
            // Sử dụng ProductDAO để thêm sản phẩm
            ProductDAO productDAO = new ProductDAO();
            productDAO.addProduct(product, categoryChoice);  // Giả sử có categoryId để liên kết
            System.out.println("Sản phẩm đã được thêm.");
        }
    }


    // Xem danh sách sản phẩm
    private static void viewProducts() {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();  // Lấy tất cả sản phẩm từ cơ sở dữ liệu
        for (Product product : products) {
            product.displayProductDetails();
        }
    }

    private static void updateProduct(Scanner scanner) {
        System.out.println("Nhập ID sản phẩm cần sửa:");
        int productId = scanner.nextInt();
        // Giống như addProduct, sửa lại thông tin sản phẩm
        // Update thông tin sản phẩm trong ProductDAO
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.println("Nhập ID sản phẩm cần xóa:");
        int productId = scanner.nextInt();

        ProductDAO productDAO = new ProductDAO();
        productDAO.deleteProduct(productId);
        System.out.println("Đã xóa sản phẩm.");
    }

    // Quản lý người dùng (dành cho Admin)
    private static void manageUsers(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Quản lý người dùng ---");
            System.out.println("1. Thêm người dùng");
            System.out.println("2. Sửa người dùng");
            System.out.println("3. Xóa người dùng");
            System.out.println("4. Xem danh sách người dùng");
            System.out.println("5. Thoát");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addUser(scanner);  // Thêm người dùng
                    break;
                case 2:
                    updateUser(scanner);  // Sửa người dùng
                    break;
                case 3:
                    deleteUser(scanner);  // Xóa người dùng
                    break;
                case 4:
                    viewUsers();  // Xem danh sách người dùng
                    break;
                case 5:
                    exit = true;  // Thoát
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void addUser(Scanner scanner) {
        System.out.println("Nhập tên người dùng:");
        String username = scanner.next();
        System.out.println("Nhập mật khẩu:");
        String password = scanner.next();


        // Thêm vào CSDL thông qua UserDAO
        UserDAO userDAO = new UserDAO();
        userDAO.addUser(new User(username, password));
        System.out.println("Đã thêm người dùng.");
    }

    private static void updateUser(Scanner scanner) {
        System.out.println("Nhập ID người dùng cần sửa:");
        int userId = scanner.nextInt();
        System.out.println("Nhập tên người dùng mới:");
        String username = scanner.next();
        System.out.println("Nhập mật khẩu mới:");
        String password = scanner.next();
        System.out.println("Nhập email mới:");
        String email = scanner.next();

        // Cập nhật người dùng
        UserDAO userDAO = new UserDAO();
        userDAO.updateUser(new User(userId, username, password, email));
        System.out.println("Đã cập nhật người dùng.");
    }

    private static void deleteUser(Scanner scanner) {
        System.out.println("Nhập ID người dùng cần xóa:");
        int userId = scanner.nextInt();

        // Xóa người dùng
        UserDAO userDAO = new UserDAO();
        userDAO.deleteUser(userId);
        System.out.println("Đã xóa người dùng.");
    }

    private static void viewUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();  // Lấy danh sách người dùng từ CSDL
        for (User user : users) {
            System.out.println(user);  // Hiển thị thông tin người dùng
        }
    }


    private static void manageCustomers(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Quản lý khách hàng ---");
            System.out.println("1. Thêm khách hàng");
            System.out.println("2. Sửa khách hàng");
            System.out.println("3. Xóa khách hàng");
            System.out.println("4. Xem danh sách khách hàng");
            System.out.println("5. Thoát");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addCustomer(scanner);  // Thêm khách hàng
                    break;
                case 2:
                    updateCustomer(scanner);  // Sửa khách hàng
                    break;
                case 3:
                    deleteCustomer(scanner);  // Xóa khách hàng
                    break;
                case 4:
                    viewCustomers();  // Xem danh sách khách hàng
                    break;
                case 5:
                    exit = true;  // Thoát
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void addCustomer(Scanner scanner) {
        System.out.println("Nhập tên khách hàng:");
        String customerName = scanner.next();
        System.out.println("Nhập email khách hàng:");
        String email = scanner.next();

        // Thêm vào CSDL thông qua CustomerDAO
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.addCustomer(new Customer(customerName, email));
        System.out.println("Đã thêm khách hàng.");
    }

    private static void updateCustomer(Scanner scanner) {
        System.out.println("Nhập ID khách hàng cần sửa:");
        int customerId = scanner.nextInt();
        System.out.println("Nhập tên khách hàng mới:");
        String customerName = scanner.next();
        System.out.println("Nhập email khách hàng mới:");
        String email = scanner.next();

        // Cập nhật khách hàng
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.updateCustomer(new Customer(customerId, customerName, email));
        System.out.println("Đã cập nhật khách hàng.");
    }

    private static void deleteCustomer(Scanner scanner) {
        System.out.println("Nhập ID khách hàng cần xóa:");
        int customerId = scanner.nextInt();

        // Xóa khách hàng
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.deleteCustomer(customerId);
        System.out.println("Đã xóa khách hàng.");
    }

    private static void viewCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.getAllCustomers();  // Lấy danh sách khách hàng từ CSDL
        for (Customer customer : customers) {
            System.out.println(customer);  // Hiển thị thông tin khách hàng
        }
    }

}
