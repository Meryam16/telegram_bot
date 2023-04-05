let array = [
   "https://travellersworldwide.com/wp-content/uploads/2022/06/shutterstock_667548661.png.webp",
   "https://www.fodors.com/wp-content/uploads/2020/01/UltimateNorway__HERO_iStock-1127199612.jpg",
   "https://media.timeout.com/images/105172508/image.jpg",

   "https://www.avrupa.info.tr/sites/default/files/styles/standard_page_header/public/2016-08/Hungary%20Budapest.jpg?itok=nk_H6wmV",
   "https://www.reisereporter.de/uploads/media/Teaser/06/53946-reiseziele-deutschland-schloesser-neuschwanstein.jpg?v=1-4",
   "https://i.guim.co.uk/img/media/ef64b3e8184ca6903eae0ac447675625b7f5c4f5/0_38_7156_4294/master/7156.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=f250552d923cea0e8a7610de2de82d1b",

   "https://images.lbc.co.uk/images/308953?crop=16_9&width=660&relax=1&signature=NWoy-Ofslg3S2h1X1gVo2oIMROc=",
   "https://lp-cms-production.imgix.net/features/2019/04/Peles-Castle-Romania-5df966f5d6a2.jpg",
   "https://www.travelanddestinations.com/wp-content/uploads/2019/06/Seville-Places-to-visit-in-Spain.jpg",
]

let say = Math.ceil(array.length / 3)
for (let i = 0; i < say; i++) {
   let x = document.createElement('li');
   let y = document.createTextNode(i + 1);
   x.appendChild(y);
   document.querySelector('#reqem').appendChild(x)
}

document.querySelectorAll('.pic').forEach((item, index) => {
   item.children[0].src = array[index];
})

document.querySelectorAll('li').forEach(item => {
   item.addEventListener('click', function () {
      document.querySelectorAll('li').forEach(element=>{
         element.style.backgroundColor="lightgray"
      })
      item.style.backgroundColor="#E6E5A3";
      let z = Number(item.innerText)
      let image = document.getElementById("imgs")
      image.innerHTML = ''
      for (i = (z - 1) * 3; i < z * 3; i++) {
         let div = document.createElement("div")
         div.classList.add("pic")
         let img = document.createElement("img")
         img.setAttribute("src", array[i])
         div.appendChild(img)
         image.appendChild(div)
      }
   })
});
// 1 0-2
// 2 3-5
// 3 6-8
// 4 9-11


let index = 1
document.querySelector(".next").addEventListener('click', function () {
   index++
   console.log(index);
   
   if (index > say) {
      index = say;
   }
  
   else if (index <= say) {
      let image = document.getElementById("imgs")
      image.innerHTML = ''
      for (i = (index - 1) * 3; i < index * 3; i++) {
         let div = document.createElement("div")
         div.classList.add("pic")
         let img = document.createElement("img")
         img.setAttribute("src", array[i])
         div.appendChild(img)
         image.appendChild(div)
      }
   } 
   document.querySelectorAll('li').forEach(item=>{
      item.style.backgroundColor="lightgray"
      if(item.innerText==index){
         item.style.backgroundColor="#E6E5A3"
      }
   })
})

document.querySelector(".pre").addEventListener('click', function () {
   index--
   if (index < 1) {
      index = 1
   }
   else if (index > 0) {
      let image = document.getElementById("imgs")
      image.innerHTML = ''
      for (i = (index - 1) * 3; i < index * 3; i++) {
         let div = document.createElement("div")
         div.classList.add("pic")
         let img = document.createElement("img")
         img.setAttribute("src", array[i])
         div.appendChild(img)
         image.appendChild(div)
      }
   }
   document.querySelectorAll('li').forEach(item=>{
      item.style.backgroundColor="lightgray"
      if(item.innerText==index){
         item.style.backgroundColor="#E6E5A3"
      }
   })
})