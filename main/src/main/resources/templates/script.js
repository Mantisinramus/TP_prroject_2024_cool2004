async function loadTask() {
    try {
        // Замените URL на ваш API-эндпоинт
        const response = await fetch('http://localhost:8080/api/task');
        const data = await response.json();

        renderGameField(data);
    } catch (error) {
        console.error("Ошибка при загрузке задания:", error);
        alert("Не удалось загрузить задание.");
    }
}

function renderGameField(task) {
    const gameField = document.getElementById("gameField");

    // Очистка старого поля
    gameField.innerHTML = "";

    // Установка размеров сетки
    gameField.style.gridTemplateRows = `repeat(${task.rows}, 40px)`;
    gameField.style.gridTemplateColumns = `repeat(${task.cols}, 40px)`;

    // Заполнение ячеек
    for (let y = 0; y < task.rows; y++) {
        for (let x = 0; x < task.cols; x++) {
            const cell = document.createElement("div");
            cell.className = "cell";
            cell.id = `cell_${x}_${y}`;
            gameField.appendChild(cell);
        }
    }

    // Размещение игрока
    const playerCell = document.getElementById(`cell_${task.player.x}_${task.player.y}`);
    const imgPlayer = document.createElement("img");
    imgPlayer.src = "player.png"; // Замените на путь к изображению игрока
    playerCell.appendChild(imgPlayer);

    // Размещение котла
    const cauldronCell = document.getElementById(`cell_${task.cauldron.x}_${task.cauldron.y}`);
    const imgCauldron = document.createElement("img");
    imgCauldron.src = "cauldron.png"; // Замените на путь к изображению котла
    cauldronCell.appendChild(imgCauldron);

    // Размещение зелий
    task.potions.forEach(potion => {
        const potionCell = document.getElementById(`cell_${potion.x}_${potion.y}`);
        const imgPotion = document.createElement("img");
        imgPotion.src = "potion.png"; // Замените на путь к изображению зелья
        potionCell.appendChild(imgPotion);
    });

    // Размещение стен
    task.walls.forEach(wall => {
        const wallCell = document.getElementById(`cell_${wall.x}_${wall.y}`);
        const imgWall = document.createElement("img");
        imgWall.src = "wall.png"; // Замените на путь к изображению стены
        wallCell.appendChild(imgWall);
    });
}
