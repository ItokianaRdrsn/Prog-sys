<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Display Data</title>
</head>
<body>
    <h1>List of Users</h1>
    <?php
    // Connexion à la base de données
    $host = 'localhost';
    $db = 'testdb'; // Nom de la base de données
    $user = 'root';
    $password = 'root';
    $conn = new mysqli($host, $user, $password, $db);

    // Vérifier la connexion
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Récupérer les données de la table
    $sql = "SELECT id, name, email FROM users";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        echo "<table border='1'>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                </tr>";
        // Afficher chaque ligne
        while ($row = $result->fetch_assoc()) {
            echo "<tr>
                    <td>" . $row['id'] . "</td>
                    <td>" . $row['name'] . "</td>
                    <td>" . $row['email'] . "</td>
                  </tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No data found</p>";
    }

    $conn->close();
    ?>
</body>
</html>
