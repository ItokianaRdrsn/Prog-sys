<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Insert Data</title>
</head>
<body>
    <h1>Enter Your Information</h1>
    <form action="insert.php" method="POST">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required><br><br>
        
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br><br>
        
        <button type="submit">Submit</button>
    </form>
    <?php
// Vérifier si le formulaire a été soumis
if (isset($_SERVER['REQUEST_METHOD']) && $_SERVER['REQUEST_METHOD'] === 'POST') {
    // Informations de connexion à la base de données
    $host = 'localhost';
    $username = 'root';
    $password = 'root';
    $database = 'testdb'; // Nom de la base de données

    // Connexion à la base de données
    $conn = mysqli_connect($host, $username, $password, $database);

    // Vérifier la connexion
    if (!$conn) {
        die("Erreur de connexion à la base de données : " . mysqli_connect_error());
    }

    // Récupérer les données du formulaire
    $name = htmlspecialchars($_POST['name']);
    $email = htmlspecialchars($_POST['email']);

    // Préparer la requête SQL
    $query = "INSERT INTO users (name, email) VALUES ('$name', '$email')";

    // Exécuter la requête
    if (mysqli_query($conn, $query)) {
        echo "<p>Les données ont été insérées avec succès !</p>";
    } else {
        echo "<p>Erreur lors de l'insertion des données : " . mysqli_error($conn) . "</p>";
    }

    // Fermer la connexion
    mysqli_close($conn);
} else {
    echo "<p>Soumettez le formulaire pour insérer des données.</p>";
}
?>


</body>
</html>
