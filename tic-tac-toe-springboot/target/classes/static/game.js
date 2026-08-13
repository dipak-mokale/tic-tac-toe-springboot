const cells = document.querySelectorAll(".cell");
const statusText = document.getElementById("status");
const resetButton = document.getElementById("reset");

async function loadGame() {
    const response = await fetch("/api/game");
    const game = await response.json();
    render(game);
}

async function makeMove(position) {
    const response = await fetch("/api/game/move", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ position })
    });

    const data = await response.json();

    if (!response.ok) {
        alert(data.error);
        return;
    }

    render(data);
}

async function resetGame() {
    const response = await fetch("/api/game/reset", {
        method: "POST"
    });

    const game = await response.json();
    render(game);
}

function render(game) {
    game.board.forEach((value, index) => {
        cells[index].textContent = value || "";
        cells[index].disabled = value !== null || game.winner !== null || game.draw;
    });

    if (game.winner) {
        statusText.textContent = `🎉 Player ${game.winner} wins!`;
    } else if (game.draw) {
        statusText.textContent = "🤝 It's a draw!";
    } else {
        statusText.textContent = `Player ${game.currentPlayer}'s turn`;
    }
}

cells.forEach(cell => {
    cell.addEventListener("click", () => {
        makeMove(Number(cell.dataset.index));
    });
});

resetButton.addEventListener("click", resetGame);

loadGame();
