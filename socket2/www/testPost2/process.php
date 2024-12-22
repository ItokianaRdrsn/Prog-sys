<?php
// Vérifier si des données POST ont été envoyées
$nom=$_POST["name"];
$age=$_POST["age"];

    // Récupérer et afficher les données
    echo "<h1>Données reçues :</h1>";
    echo "<p>Nom : " . htmlspecialchars($nom) . "</p>";
    echo "<p>Âge : " . htmlspecialchars($age) . "</p>";

