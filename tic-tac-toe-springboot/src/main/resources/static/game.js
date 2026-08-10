const cells = document.querySelectorAll(".cell");
const statusText = document.getElementById("status");
const resetButton = document.getElementById("reset");
const startGameBtn = document.getElementById("startGame");
const playerXInput = document.getElementById("playerXName");
const playerOInput = document.getElementById("playerOName");
const playerSetup = document.getElementById("playerSetup");
const gameArea = document.getElementById("gameArea");
const viewRecordsBtn = document.getElementById("viewRecords");
const recordsModal = document.getElementById("recordsModal");
const closeModal = document.querySelector(".close");
const recordsList = document.getElementById("recordsList");
const celebrationContainer = document.getElementById("celebration");

let currentGame = null;

// Celebration Animation Functions
function createConfetti() {
    const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA502', '#00C9A7', '#FF6348'];
    const shapes = ['circle', 'square', 'triangle'];
    
    for (let i = 0; i < 50; i++) {
        const confetti = document.createElement('div');
        confetti.classList.add('confetti');
        
        const randomColor = colors[Math.floor(Math.random() * colors.length)];
        const randomShape = shapes[Math.floor(Math.random() * shapes.length)];
        
        confetti.style.background = randomColor;
        confetti.style.left = Math.random() * 100 + '%';
        confetti.style.top = '-10px';
        confetti.style.width = (Math.random() * 10 + 5) + 'px';
        confetti.style.height = (Math.random() * 10 + 5) + 'px';
        confetti.style.animation = `fall ${2 + Math.random() * 1}s linear forwards`;
        confetti.style.animationDelay = Math.random() * 0.5 + 's';
        
        celebrationContainer.appendChild(confetti);
        
        setTimeout(() => confetti.remove(), 3500);
    }
}

function createPaperPopper() {
    const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA502', '#00C9A7', '#FF6348'];
    
    // Create burst from center
    for (let i = 0; i < 40; i++) {
        const particle = document.createElement('div');
        particle.classList.add('particle', 'particle-circle');
        
        const randomColor = colors[Math.floor(Math.random() * colors.length)];
        particle.style.background = randomColor;
        
        // Random position on screen
        const centerX = window.innerWidth / 2;
        const centerY = window.innerHeight / 2;
        particle.style.left = centerX + 'px';
        particle.style.top = centerY + 'px';
        particle.style.width = (Math.random() * 12 + 4) + 'px';
        particle.style.height = particle.style.width;
        
        // Random direction
        const angle = (Math.random() * Math.PI * 2);
        const velocity = 5 + Math.random() * 15;
        const vx = Math.cos(angle) * velocity;
        const vy = Math.sin(angle) * velocity;
        
        celebrationContainer.appendChild(particle);
        
        let x = centerX;
        let y = centerY;
        let opacity = 1;
        let duration = 1000 + Math.random() * 500;
        let startTime = Date.now();
        
        function animate() {
            const elapsed = Date.now() - startTime;
            const progress = elapsed / duration;
            
            if (progress >= 1) {
                particle.remove();
                return;
            }
            
            x += vx;
            y += vy + (progress * 3); // gravity effect
            opacity = 1 - progress;
            
            particle.style.transform = `translate(${x - centerX}px, ${y - centerY}px)`;
            particle.style.opacity = opacity;
            
            requestAnimationFrame(animate);
        }
        
        animate();
    }
}

function triggerCelebration(playerName) {
    const celebrationMsg = document.createElement('div');
    celebrationMsg.classList.add('celebration-message');
    celebrationMsg.innerHTML = `🎉<br>${playerName}<br>WINS! 🎉`;
    celebrationContainer.appendChild(celebrationMsg);
    
    createConfetti();
    createPaperPopper();
    
    document.body.style.animation = 'none';
    setTimeout(() => {
        document.body.style.animation = 'pulse 0.3s ease-out';
    }, 50);
    
    setTimeout(() => {
        celebrationMsg.remove();
    }, 3000);
}

function triggerDrawCelebration() {
    const celebrationMsg = document.createElement('div');
    celebrationMsg.classList.add('celebration-message');
    celebrationMsg.style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
    celebrationMsg.innerHTML = `🤝<br>IT'S A DRAW!<br>Well Played! 👏`;
    celebrationContainer.appendChild(celebrationMsg);
    
    createConfetti();
    createPaperPopper();
    
    document.body.style.animation = 'none';
    setTimeout(() => {
        document.body.style.animation = 'pulse 0.3s ease-out';
    }, 50);
    
    setTimeout(() => {
        celebrationMsg.remove();
    }, 3000);
}

async function initializeGame() {
    const playerXName = playerXInput.value.trim() || "Player X";
    const playerOName = playerOInput.value.trim() || "Player O";

    const response = await fetch("/api/game/initialize", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ playerXName, playerOName })
    });

    const data = await response.json();
    const game = data.game;
    currentGame = game;
    playerSetup.style.display = "none";
    gameArea.style.display = "block";
    render(game);
}

async function loadGame() {
    const response = await fetch("/api/game");
    const game = await response.json();
    currentGame = game;
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

    currentGame = data;
    render(data);
    
    // Trigger celebration if game is won or drawn
    if (data.winner) {
        const playerName = data.winner === "X" ? data.playerXName : data.playerOName;
        triggerCelebration(playerName);
    } else if (data.draw) {
        triggerDrawCelebration();
    }
}

async function resetGame() {
    const response = await fetch("/api/game/reset", {
        method: "POST"
    });

    const game = await response.json();
    playerSetup.style.display = "block";
    gameArea.style.display = "none";
    render(game);
    
    // Clear any remaining celebration elements
    celebrationContainer.innerHTML = "";
}

async function loadRecords() {
    const response = await fetch("/api/game/records");
    const records = await response.json();
    const latestRecord = records[records.length - 1];
    
    if (!latestRecord) {
        recordsList.innerHTML = "<p>No game records yet.</p>";
    } else {
        recordsList.innerHTML = `
            <div class="record-item">
                <p><strong>${latestRecord.winner === "DRAW" ? "Draw" : latestRecord.winner + " won"}</strong></p>
                <p>Player X: ${latestRecord.playerXName}</p>
                <p>Player O: ${latestRecord.playerOName}</p>
                <p>Moves: ${latestRecord.moves}</p>
                <p>Date: ${new Date(latestRecord.gameDate).toLocaleString()}</p>
            </div>
        `;
    }
    
    recordsModal.style.display = "block";
}

function render(game) {
    if (game.board) {
        game.board.forEach((value, index) => {
            cells[index].textContent = value || "";
            cells[index].disabled = value !== null || game.winner !== null || game.draw;
        });
    }

    if (game.winner) {
        const playerName = game.winner === "X" ? game.playerXName : game.playerOName;
        statusText.textContent = `🎉 ${playerName} (${game.winner}) wins!`;
    } else if (game.draw) {
        statusText.textContent = "🤝 It's a draw!";
    } else {
        const playerName = game.currentPlayer === "X" ? game.playerXName : game.playerOName;
        statusText.textContent = `${playerName} (${game.currentPlayer})'s turn`;
    }
}

cells.forEach(cell => {
    cell.addEventListener("click", () => {
        makeMove(Number(cell.dataset.index));
    });
});

startGameBtn.addEventListener("click", initializeGame);
resetButton.addEventListener("click", resetGame);
viewRecordsBtn.addEventListener("click", loadRecords);

closeModal.addEventListener("click", () => {
    recordsModal.style.display = "none";
});

window.addEventListener("click", (event) => {
    if (event.target === recordsModal) {
        recordsModal.style.display = "none";
    }
});

loadGame();
