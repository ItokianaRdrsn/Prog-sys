<?php
// Vérifie si REQUEST_METHOD est défini
if (!isset($_SERVER['REQUEST_METHOD'])) {
    echo "REQUEST_METHOD n'est pas défini. Assurez-vous que ce script est exécuté via un serveur HTTP.";
    exit;
}

// Vérifie que la méthode est GET
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Vérifie si les données GET sont présentes
    if (isset($_GET['name']) && isset($_GET['email'])) {
        $name = htmlspecialchars($_GET['name']); // Protection contre les injections HTML
        $email = htmlspecialchars($_GET['email']);

        echo "<h1>Voici les données reçues :</h1>";
        echo "<p><strong>Nom :</strong> $name</p>";
        echo "<p><strong>Email :</strong> $email</p>";
    } else {
        echo "<h1>Aucune donnée n'a été reçue.</h1>";
    }
} else {
    echo "<h1>Méthode non supportée.</h1>";
}
?>
