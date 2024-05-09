package Services;

import Entities.User;
import Utils.MyDatabase;
import Entities.Produit;
import Entities.CategorieProd;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class ProduitService implements IServices<Produit> {

    private Connection connection;
    // Define the directory path for saving PDF files
    private static final String PDF_PATH = "/path/to/pdf/files/";



    public ProduitService() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override

    public void add(Produit p) throws SQLException {
        String req = "INSERT INTO produit (name, description, image, quantity, price,categorie_prod_id, user_cin) VALUES (?, ?, ?, ?, ?,?,?)";


        try (PreparedStatement stm = connection.prepareStatement(req)) {
            // Définition des valeurs à l'aide des méthodes setter
            stm.setString(1, p.getName());
            stm.setString(2, p.getDescription());
            stm.setString(3, p.getImage());
            stm.setInt(4, p.getQuantity());
            stm.setDouble(5, p.getPrice());
            stm.setInt(6,p.getCategorie().getId());
            stm.setInt(7, Integer.parseInt(p.getUser().getCin()));

            // Exécution de la requête
            stm.executeUpdate();

            System.out.println("produit ajoutée.");
        } catch (SQLException e) {
            // Gestion de l'exception SQLException
            System.err.println("Erreur lors de l'ajout de la produit : " + e.getMessage());
            // Vous pouvez également propager l'exception à la couche supérieure si nécessaire
            throw e;
        }
    }

    @Override
    public void update(Produit produit) throws SQLException {
        StringBuilder reqBuilder = new StringBuilder("UPDATE produit SET name=?, description=?, quantity=?, price=?, categorie_prod_id=?");
        List<Object> values = new ArrayList<>(Arrays.asList(produit.getName(), produit.getDescription(), produit.getQuantity(), produit.getPrice(), produit.getCategorie().getId()));

        // Check if an image path is provided
        String imagePath = produit.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            reqBuilder.append(", image=?"); // Add the image column to the query
            values.add(imagePath); // Add the image path to the values list
        }

        reqBuilder.append(" WHERE ref=?"); // Add the WHERE clause
        values.add(produit.getRef()); // Add the product reference to the values list

        String req = reqBuilder.toString();

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Set the values in the prepared statement
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i + 1, values.get(i));
            }

            preparedStatement.executeUpdate();
        }
        System.out.println("Produit modifié");
    }


    public void delete(Produit produit) throws SQLException {
        String sql = "DELETE FROM produit WHERE ref = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, produit.getRef());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting produit: " + e.getMessage());
            throw e;
        }
    }

    @Override



    public List<Produit> show() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.*, u.firstname, u.lastname, c.id AS category_id, c.nom_ca AS category_name " +
                "FROM produit p " +
                "JOIN user u ON p.user_cin = u.cin " +
                "JOIN categorie_prod c ON p.categorie_prod_id = c.id";
        try (PreparedStatement stm = connection.prepareStatement(req);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                CategorieProd categorie = new CategorieProd(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User();
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                Produit produit = new Produit(
                        rs.getInt("ref"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        categorie,
                        user
                );
                produits.add(produit);
            }
        }
        return produits;
    }


    @Override
    public Produit afficher(Produit produit) throws SQLException {
        return null;
    }

    @Override
    public void ajouter(Produit produit) throws SQLException {

    }

    @Override
    public void modifier(Produit produit) throws SQLException {

    }

    @Override
    public void supprimer(Produit produit) throws SQLException {

    }

    @Override
    public List<Produit> afficher() throws SQLException {
        return null;
    }


    public List<Produit> getAllProduits() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.*, u.firstname, u.lastname, c.id AS category_id, c.nom_ca AS category_name " +
                "FROM produit p " +
                "JOIN categorie_prod c ON p.categorie_prod_id = c.id " +
                "JOIN user u ON p.user_cin = u.cin";
        try (PreparedStatement stm = connection.prepareStatement(req);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                CategorieProd categorie = new CategorieProd(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User();
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                Produit produit = new Produit(
                        rs.getInt("ref"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),

                        categorie,
                        user
                );
                produits.add(produit);
            }
        }
        return produits;
    }



    public Produit getProduitById(int produitId) throws SQLException {
        Produit produit = null;
        String req = "SELECT p.*, c.nom_ca FROM produit p JOIN categorie_prod c ON p.categorie_prod_id = c.id WHERE p.ref = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, produitId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    produit = new Produit();
                    produit.setRef(rs.getInt("ref"));
                    produit.setName(rs.getString("name"));
                    produit.setDescription(rs.getString("description"));
                    produit.setImage(rs.getString("image"));
                    produit.setQuantity(rs.getInt("quantity"));
                    produit.setPrice(rs.getDouble("price"));
                    int categoryId = rs.getInt("categorie_prod_id");
                    CategorieService categoryServices = new CategorieService();
                    CategorieProd categorie = categoryServices.getCategorieById(categoryId);
                    produit.setCategorie(categorie);
                }
            }
        }
        return produit;
    }


    @Override
    public void supprimer(int id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
    }

    public static void savePDF(String fileName) {
        String filePath = PDF_PATH + fileName + ".pdf";
        File file = new File(filePath);
        if (file.exists()) {
            // Générer un nom de fichier unique si le fichier existe déjà
            fileName = generateUniqueFileName(fileName);
            filePath = PDF_PATH + fileName + ".pdf";
            file = new File(filePath);
        }
    }

    public static void generatePDF(List<Produit> produits, String fileName) throws IOException {
        String filePath = PDF_PATH + fileName + ".pdf";
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Ajouter la date et l'heure actuelles au contenu du document
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String currentDate = "Date: " + dtf.format(now);
                float xDate = 50; // Position X de la date
                float yDate = page.getMediaBox().getHeight() - 200; // Position Y de la date
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(xDate, yDate);
                contentStream.showText(currentDate);
                contentStream.endText();

                // Dessiner une ligne pour séparer la date du reste du contenu
                float lineY = yDate - 10;
                contentStream.moveTo(xDate, lineY);
                contentStream.lineTo(xDate + 150, lineY);
                contentStream.stroke();

                // Définir les coordonnées pour le début du tableau
                float xTable = 50; // Position X du coin supérieur gauche du tableau
                float yTable = page.getMediaBox().getHeight() - 250; // Position Y du coin supérieur gauche du tableau

                // Créer un tableau pour afficher les détails des cabinets
                float tableWidth = page.getMediaBox().getWidth() - 2 * xTable;
                float tableHeight = 100;
                float cellMargin = 10;
                float cellWidth = (tableWidth - 4 * cellMargin) / 4;
                float cellHeight = 20;

                // Dessiner une ligne pour séparer l'en-tête du contenu du tableau
                float headerLineY = yTable + cellHeight + cellMargin / 2;
                contentStream.moveTo(xTable, headerLineY);
                contentStream.lineTo(xTable + tableWidth, headerLineY);
                contentStream.stroke();

                // Ajouter les en-têtes de colonne avec du texte vert
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float yText = yTable - cellHeight / 2;
                contentStream.setNonStrokingColor(Color.GREEN);
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin, yText);
                contentStream.showText("Name");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin + cellWidth + cellMargin, yText);
                contentStream.showText("Price");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin + 2 * (cellWidth + cellMargin), yText);
                contentStream.showText("Quantity");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin + 3 * (cellWidth + cellMargin), yText);
                contentStream.showText("Categorie");
                contentStream.endText();

                // Remplir le tableau avec les détails des cabinets
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                for (Produit produit : produits) {
                    contentStream.beginText();
                    yText -= cellHeight;
                    contentStream.newLineAtOffset(xTable + cellMargin, yText);
                    contentStream.showText("Product Name: " + produit.getName());
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xTable + cellMargin + cellWidth + cellMargin, yText);
                    contentStream.showText("Price: " + produit.getPrice());
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xTable + cellMargin + 2 * (cellWidth + cellMargin), yText);
                    contentStream.showText("Quantity: " + produit.getQuantity());
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xTable + cellMargin + 3 * (cellWidth + cellMargin), yText);

                }
            }
            document.save(filePath);
        }
    }

    private static String generateUniqueFileName(String fileName) {
        // Ajouter une logique pour générer un nom de fichier unique
        // Par exemple, vous pouvez ajouter un timestamp à la fin du nom de fichier
        return fileName + "_" + System.currentTimeMillis();
    }

}