const initModals = () => {
    const triggers = document.querySelectorAll('[data-target]')
    const closeButtons = document.querySelectorAll('.modal__close')

    closeButtons.forEach(closeButton => {
        closeButton.addEventListener('click', () => {
            closeButton.parentElement.classList.remove('modal--visible')
        })
    })

    triggers.forEach(trigger => {
        trigger.addEventListener('click', () => {
            const modal = document.getElementById(trigger.dataset.target)
            modal.classList.add('modal--visible')
        })
    })
}

const initMasks = () => {
    const inputs = document.querySelectorAll('[data-mask]')
    inputs.forEach(input => new IMask(input, {mask: input.dataset.mask}))
}

const initFilesInput = () => {
    const inputs = document.querySelectorAll('input[type="file"]')
    inputs.forEach(input => input.addEventListener('change', () => input.dataset.placeholder = input.files[0].name))
}

document.addEventListener('DOMContentLoaded', () => {
    initModals()
    initMasks()
    initFilesInput()
})