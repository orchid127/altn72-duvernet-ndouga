const modal = document.getElementById('modal-incident');
const openBtn = document.getElementById('open-modal');
const closeBtn = document.getElementById('close-modal');

if (modal && openBtn) {
    const open = () => modal.classList.add('open');
    const close = () => modal.classList.remove('open');

    openBtn.addEventListener('click', open);
    closeBtn.addEventListener('click', close);

    modal.addEventListener('click', (e) => {
        if (e.target === modal) close();
    });
}