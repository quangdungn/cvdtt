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
import factory.users.*;
import model.customers.*;

public class MainApp {

    public static void main(String[] args) {
        User loggedInUser = null;

        // Đăng nhập người dùng
        while (loggedInUser == null) {
            loggedInUser = LoginSystem.login();  // Đăng nhập hệ thống
            if (loggedInUser == null) {
                System.out.println("Vui lòng nhập lại.");
            }
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
        System.out.println("3. Sửa đơn hàng");
        System.out.println("4. Xóa đơn hàng");
        System.out.println("5. Thoát");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                createOrder(scanner, loggedInUser);  // Tạo đơn hàng
                break;
            case 2:
                viewOrders();  // Xem danh sách đơn hàng
                break;
            case 3:
                updateOrder(scanner);  // Sửa đơn hàng
                break;
            case 4:
                deleteOrder(scanner);  // Xóa đơn hàng
                break;
            case 5:
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
// Tính tổng số tiền của đơn hàng
        double totalAmount = 0;
        System.out.println("\nSản phẩm trong đơn hàng:");
        for (Product product : orderProducts) {
            System.out.println(product.getName() + " - Giá: " + product.getPrice() + " VND");
            totalAmount += product.getPrice();
        }
        System.out.println("Tổng số tiền: " + totalAmount + " VND");

// Chọn phương thức thanh toán
        System.out.println("Chọn phương thức thanh toán:");
        System.out.println("1. Chuyển khoản ngân hàng");
        System.out.println("2. Thẻ tín dụng");
        System.out.println("3. PayPal");
        System.out.println("4. Stripe");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine();

// Khởi tạo PaymentStrategy
        PaymentStrategy paymentStrategy = null;

        switch (paymentChoice) {
            case 1:
                paymentStrategy = new BankTransferPayment();
                break;
            case 2:
                paymentStrategy = new CreditCardPayment();
                break;
            case 3:
                paymentStrategy = new PayPalPaymentAdapter(new PayPalPayment());  // Sử dụng PayPal Adapter
                break;
            case 4:
                paymentStrategy = new StripePaymentAdapter(new StripePayment());  // Sử dụng Stripe Adapter
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ, mặc định chọn 'Chuyển khoản ngân hàng'");
                paymentStrategy = new BankTransferPayment();
                break;
        }

// Sử dụng PaymentContext để thực hiện thanh toán
        PaymentContext paymentContext = new PaymentContext(paymentStrategy);
        paymentContext.executePayment(totalAmount);

        System.out.println("Đơn hàng đã được tạo thành công.");
    }
    private static void updateOrder(Scanner scanner) {
        // Yêu cầu người dùng nhập ID đơn hàng cần cập nhật
        System.out.println("Nhập ID đơn hàng cần sửa:");
        int orderId = scanner.nextInt();
        scanner.nextLine();  // Đọc ký tự newline còn sót lại

        // Yêu cầu người dùng nhập các thông tin mới cho đơn hàng
        System.out.println("Nhập customer_id mới:");
        int customerId = scanner.nextInt();

        System.out.println("Nhập tổng số tiền mới:");
        double totalAmount = scanner.nextDouble();
        scanner.nextLine();  // Đọc ký tự newline còn sót lại

        System.out.println("Nhập ngày đặt hàng mới (yyyy-MM-dd):");
        String orderDate = scanner.nextLine();

        System.out.println("Nhập trạng thái đơn hàng mới (Pending, Shipped, Delivered, Cancelled):");
        String status = scanner.nextLine();

        // Yêu cầu người dùng chọn kiểu đơn hàng (Express hoặc Standard)
        System.out.println("Chọn loại đơn hàng:");
        System.out.println("1. Express Order");
        System.out.println("2. Standard Order");
        int orderType = scanner.nextInt();

        // Tạo đối tượng Order từ Factory
        Order order = null;

        // Dùng Factory để tạo đối tượng đơn hàng đúng kiểu
        if (orderType == 1) {
            order = new ExpressOrder(customerId, totalAmount, orderDate, status);
        } else if (orderType == 2) {
            order = new StandardOrder(customerId, totalAmount, orderDate, status);
        } else {
            System.out.println("Lựa chọn không hợp lệ.");
            return;
        }

        // Gọi phương thức updateOrder trong OrderDAO để cập nhật đơn hàng
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.updateOrder(order, orderId);  // Cập nhật đơn hàng trong cơ sở dữ liệu

        System.out.println("Đơn hàng đã được cập nhật.");
    }

    private static void deleteOrder(Scanner scanner) {
        System.out.println("Nhập ID đơn hàng cần xóa:");
        int orderId = scanner.nextInt();

        // Gọi phương thức deleteOrder trong OrderDAO để xóa đơn hàng
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.deleteOrder(orderId);  // Xóa đơn hàng từ cơ sở dữ liệu

        System.out.println("Đơn hàng đã được xóa.");
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
        System.out.println("Nhập loại người dùng (Admin/Staff):");
        String userType = scanner.next();

        System.out.println("Nhập tên người dùng:");
        String username = scanner.next();
        System.out.println("Nhập mật khẩu:");
        String password = scanner.next();
        System.out.println("Nhập email:");
        String email = scanner.next();
        System.out.println("Nhập số điện thoại:");
        String phoneNumber = scanner.next();

        User user = null;


        // Sử dụng Factory để tạo đối tượng User tương ứng
        if ("Admin".equalsIgnoreCase(userType)) {
            // Sử dụng AdminFactory để tạo Admin
            UserFactory adminFactory = new AdminFactory();
            user = adminFactory.createUser(username, password, email, phoneNumber);
        } else if ("Staff".equalsIgnoreCase(userType)) {
            // Sử dụng StaffFactory để tạo Staff
            UserFactory staffFactory = new StaffFactory();
            user = staffFactory.createUser(username, password, email, phoneNumber);
        } else {
            System.out.println("Loại người dùng không hợp lệ.");
            return;
        }

        // Sử dụng UserDAO để thêm người dùng vào CSDL
        UserDAO userDAO = new UserDAO();
        userDAO.addUser(user);
        System.out.println(userType + " đã được thêm.");
    }


    private static void updateUser(Scanner scanner) {
        System.out.println("Nhập ID người dùng cần sửa:");
        int userId = scanner.nextInt();
        scanner.nextLine();  // Đọc newline sau khi nhập số

        System.out.println("Nhập loại người dùng mới (Admin/Staff):");
        String userType = scanner.next();

        System.out.println("Nhập tên người dùng mới:");
        String username = scanner.next();
        System.out.println("Nhập mật khẩu mới:");
        String password = scanner.next();
        System.out.println("Nhập email mới:");
        String email = scanner.next();
        System.out.println("Nhập số điện thoại mới:");
        String phoneNumber = scanner.next();

        User user = null;

        // Sử dụng Factory để tạo đối tượng User tương ứng
        if ("Admin".equalsIgnoreCase(userType)) {
            // Sử dụng AdminFactory để tạo Admin
            UserFactory adminFactory = new AdminFactory();
            user = adminFactory.createUser(username, password, email, phoneNumber);
        } else if ("Staff".equalsIgnoreCase(userType)) {
            // Sử dụng StaffFactory để tạo Staff
            UserFactory staffFactory = new StaffFactory();
            user = staffFactory.createUser(username, password, email, phoneNumber);
        } else {
            System.out.println("Loại người dùng không hợp lệ.");
            return;
        }

        // Cập nhật người dùng trong CSDL
        UserDAO userDAO = new UserDAO();
        userDAO.updateUser(userId, user);  // Giả sử bạn đã cập nhật phương thức updateUser trong UserDAO
        System.out.println("Đã cập nhật thông tin người dùng.");
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
        // Yêu cầu người dùng nhập thông tin đầy đủ cho khách hàng
        System.out.println("Nhập tên khách hàng:");
        String fullName = scanner.nextLine();  // Đọc tên khách hàng

        System.out.println("Nhập số điện thoại khách hàng:");
        String phoneNumber = scanner.nextLine();  // Đọc số điện thoại

        System.out.println("Nhập email khách hàng:");
        String email = scanner.nextLine();  // Đọc email khách hàng

        System.out.println("Nhập địa chỉ khách hàng:");
        String address = scanner.nextLine();  // Đọc địa chỉ khách hàng

        // Thêm khách hàng vào cơ sở dữ liệu thông qua CustomerDAO
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.addCustomer(new Customer(fullName, phoneNumber, email, address));

        System.out.println("Đã thêm khách hàng.");
    }

    private static void updateCustomer(Scanner scanner) {
        System.out.println("Nhập ID khách hàng cần sửa:");
        int customerId = scanner.nextInt();
        scanner.nextLine();  // Đọc ký tự newline còn sót lại

        System.out.println("Nhập tên khách hàng mới:");
        String customerName = scanner.nextLine();

        System.out.println("Nhập số điện thoại khách hàng mới:");
        String phoneNumber = scanner.nextLine();

        System.out.println("Nhập email khách hàng mới:");
        String email = scanner.nextLine();

        System.out.println("Nhập địa chỉ khách hàng mới:");
        String address = scanner.nextLine();

        // Cập nhật thông tin khách hàng
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = new Customer(customerName, phoneNumber, email, address);
        customer.setCustomerId(customerId);  // Gán ID cho khách hàng cần sửa

        customerDAO.updateCustomer(customerId, customer);  // Gọi phương thức cập nhật trong CustomerDAO
        System.out.println("Đã cập nhật thông tin khách hàng.");
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
