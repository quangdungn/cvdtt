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

        while (loggedInUser == null) {
            loggedInUser = LoginSystem.login();
            if (loggedInUser == null) {
                System.out.println("Vui lòng nhập lại.");
            }
        }

        System.out.println("Đã đăng nhập với quyền: " + loggedInUser.getRole());

        Scanner scanner = new Scanner(System.in);

        if (loggedInUser.getRole().equals("Staff")) {
            handleStaff(scanner, loggedInUser);
        } else if (loggedInUser.getRole().equals("Admin")) {
            handleAdmin(scanner, loggedInUser);
        }
    }

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
                    manageOrders(scanner, loggedInUser);
                    break;
                case 2:
                    manageProducts(scanner);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

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
                    manageUsers(scanner);
                    break;
                case 2:
                    manageCustomers(scanner);
                    break;
                case 3:
                    manageOrders(scanner, loggedInUser);
                    break;
                case 4:
                    manageProducts(scanner);
                    break;
                case 5:
                    exit = true;
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
                viewOrders();
                break;
            case 3:
                updateOrder(scanner);
                break;
            case 4:
                deleteOrder(scanner);
                break;
            case 5:
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
        }
    }

    private static void createOrder(Scanner scanner, User loggedInUser) {
        System.out.println("Nhập thông tin đơn hàng:");

        ProductDAO productDAO = new ProductDAO();
        System.out.println("Danh sách sản phẩm:");
        List<Product> products = productDAO.getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.print((i + 1) + ". ");
            products.get(i).displayProductDetails();
        }

        List<Product> orderProducts = new ArrayList<>();
        while (true) {
            System.out.print("Nhập số sản phẩm bạn muốn thêm vào đơn hàng (hoặc nhập 0 để kết thúc): ");
            int productChoice = scanner.nextInt();
            if (productChoice == 0) {
                break;
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

        PaymentContext paymentContext = new PaymentContext(paymentStrategy);
        paymentContext.executePayment(totalAmount);

        System.out.println("Đơn hàng đã được tạo thành công.");
    }
    private static void updateOrder(Scanner scanner) {
        System.out.println("Nhập ID đơn hàng cần sửa:");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Nhập customer_id mới:");
        int customerId = scanner.nextInt();

        System.out.println("Nhập tổng số tiền mới:");
        double totalAmount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Nhập ngày đặt hàng mới (yyyy-MM-dd):");
        String orderDate = scanner.nextLine();

        System.out.println("Nhập trạng thái đơn hàng mới (Pending, Shipped, Delivered, Cancelled):");
        String status = scanner.nextLine();

        System.out.println("Chọn loại đơn hàng:");
        System.out.println("1. Express Order");
        System.out.println("2. Standard Order");
        int orderType = scanner.nextInt();

        Order order = null;

        if (orderType == 1) {
            order = new ExpressOrder(customerId, totalAmount, orderDate, status);
        } else if (orderType == 2) {
            order = new StandardOrder(customerId, totalAmount, orderDate, status);
        } else {
            System.out.println("Lựa chọn không hợp lệ.");
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        orderDAO.updateOrder(order, orderId);

        System.out.println("Đơn hàng đã được cập nhật.");
    }

    private static void deleteOrder(Scanner scanner) {
        System.out.println("Nhập ID đơn hàng cần xóa:");
        int orderId = scanner.nextInt();

        OrderDAO orderDAO = new OrderDAO();
        orderDAO.deleteOrder(orderId);

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
                    addProduct(scanner);
                    break;
                case 2:
                    updateProduct(scanner);
                    break;
                case 3:
                    deleteProduct(scanner);
                    break;
                case 4:
                    viewProducts();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.println("Chọn loại sản phẩm:");
        System.out.println("1. Điện tử");
        System.out.println("2. Quần áo");

        int categoryChoice = scanner.nextInt();
        Product product = null;

        if (categoryChoice == 1) {
            scanner.nextLine();
            System.out.print("Nhập tên sản phẩm: ");
            String productName = scanner.nextLine();
            System.out.print("Nhập giá sản phẩm: ");
            double price = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Nhập số lượng: ");
            int stockQuantity = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nhập mô tả: ");
            String description = scanner.nextLine();
            System.out.print("Nhập thương hiệu: ");
            String brand = scanner.nextLine();

            product = new Electronics(productName, price, description, stockQuantity, brand);
        } else if (categoryChoice == 2) {
            scanner.nextLine();
            System.out.print("Nhập tên sản phẩm: ");
            String productName = scanner.nextLine();
            System.out.print("Nhập giá sản phẩm: ");
            double price = scanner.nextDouble();
            System.out.print("Nhập số lượng: ");
            int stockQuantity = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nhập mô tả: ");
            String description = scanner.nextLine();
            System.out.print("Nhập size: ");
            String size = scanner.nextLine();

            product = new Clothing(productName, price, description, stockQuantity, size);
        } else {
            System.out.println("Lựa chọn không hợp lệ.");
        }

        if (product != null) {
            ProductDAO productDAO = new ProductDAO();
            productDAO.addProduct(product, categoryChoice);
            System.out.println("Sản phẩm đã được thêm.");
        }
    }

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
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.println("Nhập ID sản phẩm cần xóa:");
        int productId = scanner.nextInt();

        ProductDAO productDAO = new ProductDAO();
        productDAO.deleteProduct(productId);
        System.out.println("Đã xóa sản phẩm.");
    }

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
                    addUser(scanner);
                    break;
                case 2:
                    updateUser(scanner);
                    break;
                case 3:
                    deleteUser(scanner);
                    break;
                case 4:
                    viewUsers();
                    break;
                case 5:
                    exit = true;
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

        UserDAO userDAO = new UserDAO();
        userDAO.addUser(user);
        System.out.println(userType + " đã được thêm.");
    }

    private static void updateUser(Scanner scanner) {
        System.out.println("Nhập ID người dùng cần sửa:");
        int userId = scanner.nextInt();
        scanner.nextLine();

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

        if ("Admin".equalsIgnoreCase(userType)) {
            UserFactory adminFactory = new AdminFactory();
            user = adminFactory.createUser(username, password, email, phoneNumber);
        } else if ("Staff".equalsIgnoreCase(userType)) {
            UserFactory staffFactory = new StaffFactory();
            user = staffFactory.createUser(username, password, email, phoneNumber);
        } else {
            System.out.println("Loại người dùng không hợp lệ.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        userDAO.updateUser(userId, user);
        System.out.println("Đã cập nhật thông tin người dùng.");
    }

    private static void deleteUser(Scanner scanner) {
        System.out.println("Nhập ID người dùng cần xóa:");
        int userId = scanner.nextInt();

        UserDAO userDAO = new UserDAO();
        userDAO.deleteUser(userId);
        System.out.println("Đã xóa người dùng.");
    }

    private static void viewUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            System.out.println(user);
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
                    addCustomer(scanner);
                    break;
                case 2:
                    updateCustomer(scanner);
                    break;
                case 3:
                    deleteCustomer(scanner);
                    break;
                case 4:
                    viewCustomers();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void addCustomer(Scanner scanner) {
        System.out.println("Nhập tên khách hàng:");
        String fullName = scanner.nextLine();

        System.out.println("Nhập số điện thoại khách hàng:");
        String phoneNumber = scanner.nextLine();

        System.out.println("Nhập email khách hàng:");
        String email = scanner.nextLine();

        System.out.println("Nhập địa chỉ khách hàng:");
        String address = scanner.nextLine();

        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.addCustomer(new Customer(fullName, phoneNumber, email, address));

        System.out.println("Đã thêm khách hàng.");
    }

    private static void updateCustomer(Scanner scanner) {
        System.out.println("Nhập ID khách hàng cần sửa:");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Nhập tên khách hàng mới:");
        String customerName = scanner.nextLine();

        System.out.println("Nhập số điện thoại khách hàng mới:");
        String phoneNumber = scanner.nextLine();

        System.out.println("Nhập email khách hàng mới:");
        String email = scanner.nextLine();

        System.out.println("Nhập địa chỉ khách hàng mới:");
        String address = scanner.nextLine();

        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = new Customer(customerName, phoneNumber, email, address);
        customer.setCustomerId(customerId);

        customerDAO.updateCustomer(customerId, customer);
        System.out.println("Đã cập nhật thông tin khách hàng.");
    }

    private static void deleteCustomer(Scanner scanner) {
        System.out.println("Nhập ID khách hàng cần xóa:");
        int customerId = scanner.nextInt();

        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.deleteCustomer(customerId);
        System.out.println("Đã xóa khách hàng.");
    }

    private static void viewCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

}
