<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Exemple de page PHP</title>
    
</head>
<body>
    <h1>Exemple de génération dynamique avec PHP</h1>

    <?php
    // Tableau associatif
    $students = [
        ["name" => "Alice", "score" => 85],
        ["name" => "Bob", "score" => 73],
        ["name" => "Charlie", "score" => 92],
        ["name" => "Diana", "score" => 88],
        ["name" => "Ethan", "score" => 67],
    ];

    echo "<h2>Liste des étudiants et leurs notes</h2>";

    // Table HTML générée dynamiquement
    echo "<table>";
    echo "<thead>
            <tr>
                <th>Nom</th>
                <th>Score</th>
                <th>Statut</th>
            </tr>
          </thead>";
    echo "<tbody>";

    foreach ($students as $index => $student) {
        $status = $student['score'] >= 75 ? "Admis" : "Échoué";
        $rowClass = $index % 2 === 0 ? "even" : "odd";

        echo "<tr class='$rowClass'>
                <td>{$student['name']}</td>
                <td>{$student['score']}</td>
                <td>$status</td>
              </tr>";
    }

    echo "</tbody>";
    echo "</table>";

    // Afficher les nombres pairs de 1 à 10
    echo "<h2>Nombres pairs de 1 à 10</h2>";
    for ($i = 1; $i <= 10; $i++) {
        if ($i % 2 == 0) {
            echo "<span style='margin-right: 10px;'>$i</span>";
        }
    }

    // Une boucle while
    echo "<h2>Compter jusqu'à 5 avec une boucle while</h2>";
    $counter = 1;
    while ($counter <= 5) {
        echo "<p>Compteur : $counter</p>";
        $counter++;
    }
    ?>
</body>
</html>
