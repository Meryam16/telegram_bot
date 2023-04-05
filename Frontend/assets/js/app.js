
const div = document.querySelector("div")
const btn = document.querySelector(".refresh-btn")
const panel = document.querySelector(".panel")


const getUserData = async () =>{
    panel.innerHTML = ''
    await fetch("http://localhost:2020/getall")
    .then(res=>res.json())
    .then(data=>{
        data.forEach(element => {
            var session = element.sessions
            var answers = JSON.parse(session[session.length-1].answers)
            var list = '';
            for (let i = 0; i < Object.values(answers).length; i++) {
                let key = Object.keys(answers)[i]
                let val = Object.values(answers)[i]
                list+= `<div class=" d-flex justify-content-between">
                            <p class="w-75 mb-0">${key}</p>
                            <p class="mb-0">${val}</p>
                        </div>
                        <div class="line"></div>
                        `
            }
             let box = `<div class="cols panel-box">
                            <div class="border">
                            ${list}
                            <div class="mb-2">
                            <a class="offer-btn d-flex justify-content-center align-items-center" data-bs-toggle="collapse" href="#collapseExample${element.id}" role="button" aria-expanded="false" aria-controls="collapseExample">
                                <span class="mr-2"> Təklif göndər</span><i class="fas fa-angle-down"></i>
                            </a>
                            </div>
                
                            <div class="collapse" id="collapseExample${element.id}">
                            <textarea class="w-100" name="" id="m${element.id}" cols="30" rows="10"></textarea>
                            <div class="send-btn">
                                <button onclick="postData(${element.id})" id="${element.id}">Göndər</button>
                            </div>
                            </div>
                            </div>
                        </div>`
        panel.innerHTML += box;
    
         });
    })
}
getUserData();



const postData = (id) =>{
    const textarea = document.querySelector(`#m${id}`)
    fetch("http://localhost:2020/post",{
        method:"Post",
        body:JSON.stringify({text:textarea.value,chatId:`${id}`})
    })
}

btn.addEventListener("click",getUserData)




    


// fetch("http://localhost:8080/redis",{
//     method:'Get',
//     headers:headers
// })
// .then(res=>res.json())
// .then(data=>console.log(data))
// .catch(err=>console.log(err))
