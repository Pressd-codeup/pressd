// Menu expanding feature
//Navbar
const menu = document.querySelector('.menu');
const navbar = document.querySelector('.navbar');

menu.addEventListener('click',() => {
    navbar.classList.toggle('change');
    menu.classList.toggle('change');
});
//End of Navbar

// Section 2 Video
const video = document.querySelector('.video');
const btn = document.querySelector('.buttons i');
const bar = document.querySelector('.video-bar');

const playPause = () => {
    video.play();
}

btn.addEventListener('click', () => {
    playPause();
});
// End of Section 2 Video