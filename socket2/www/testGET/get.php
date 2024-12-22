<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire GET</title>
</head>
<body>
    <h1>Formulaire GET</h1>
    <form action="process.php" method="GET">
        <label for="name">Nom :</label>
        <input type="text" id="name" name="name" required><br><br>

        <label for="email">Email :</label>
        <input type="email" id="email" name="email" required><br><br>

        <button type="submit">Envoyer</button>
    </form>
</body>
</html>
