import Vue from 'vue'
import VueRouter from 'vue-router'
import Entrance from '../Entrance.vue'
import Home from '../views/Home.vue'
import Admin from '../views/Admin.vue'
import NewArticle from '../components/Articles/NewArticle.vue'
import ArticleDetail from '../components/Articles/ArticleDetail.vue'
import SignIn from '../views/SignIn.vue'
import SignUp from '../views/SignUp.vue'
import SignUpCertification from '../components/Accounts/SignUpCertification.vue'
import Profile from '../components/Accounts/Profile.vue'
import User from '../components/Accounts/User.vue'
import ChangeUserInfo from '../components/Accounts/ChangeUserInfo.vue'
import ValidateEmail from '../components/Accounts/ValidateEmail.vue'
import ChangePWD from '../components/Accounts/ChangePWD.vue'
import Barters from '../views/Barters.vue'
import BartersItemDetail from '../components/Barters/BartersItemDetail.vue'
import Checks from '../views/Checks.vue'
import Eithers from '../views/Eithers.vue'
import NewEither from '../components/Eithers/NewEither.vue'
import EitherDetail from '../components/Eithers/EitherDetail.vue'
import Tips from '../views/Tips.vue'
import TipsItemDetail from '../components/Tips/TipsItemDetail.vue'
import NewTip from '../components/Tips/NewTip.vue'
import PageNotFound from '../views/PageNotFound.vue'
import SocialLogin from '../components/Accounts/SocialLogin.vue'
import SocialLoginFirst from '../components/Accounts/SocialLoginFirst.vue'
import LiveChat from '../views/LiveChat.vue'

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Entrance',
    component: Entrance,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next({name: 'Home'})
      } else { next() }
    }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
  },
  {
    path: '/main',
    name: 'Home',
    component: Home,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/main/new',
    name: 'NewArticle',
    component: NewArticle,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/main/update',
    name: 'UpdateArticle',
    component: NewArticle,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({ name: 'SignIn' }) }
    }
  },
  {
    path: '/main/:article_id',
    name: 'ArticleDetail',
    component: ArticleDetail,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/accounts/signin',
    name: 'SignIn',
    component: SignIn,
    beforeEnter(to, from, next) {
      if (!Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'Home'}) }
    }
  },
  {
    path: '/accounts/signup',
    name: 'SignUp',
    component: SignUp,
    beforeEnter(to, from, next) {
      if (!Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'Home' }) }
    }
  },
  {
    path: '/accounts/email',
    name: 'ValidateEmail',
    component: ValidateEmail
  },
  {
    path: '/accounts/setnewpwd',
    name: 'ChangePWD',
    component: ChangePWD,
  },
  {
    path: '/accounts/user',
    name: 'Profile',
    component: Profile,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/accounts/certification',
    name: 'SignUpCertification',
    component: SignUpCertification
  },
  {
    path: '/accounts/social/first',
    name: 'SocialLoginFirst',
    component: SocialLoginFirst
  },
  {
    path: '/accounts/social/login',
    name: 'SocialLogin',
    component: SocialLogin
  },
  {
    path: '/accounts/:user_id',
    name: 'User',
    component: User,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/accounts/:user_id/info',
    name: 'ChangeUserInfo',
    component: ChangeUserInfo,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/barters',
    name: 'Barters',
    component: Barters,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/barters/:item_id',
    name: 'BartersItemDetail',
    component: BartersItemDetail,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/checks',
    name: 'Checks',
    component: Checks,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/eithers',
    name: 'Eithers',
    component: Eithers,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/eithers/new',
    name: 'NewEither',
    component: NewEither,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/eithers/:either_id',
    name: 'EitherDetail',
    component: EitherDetail,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/tips',
    name: 'Tips',
    component: Tips,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/tips/new',
    name: 'NewTip',
    component: NewTip,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '/tips/update',
    name: 'UpdateTip',
    component: NewTip,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({ name: 'SignIn' }) }
    }
  },
  {
    path: '/tips/:tip_id',
    name: 'TipsItemDetail',
    component: TipsItemDetail,
    props: true,
    beforeEnter(to, from, next) {
      if (Vue.$cookies.isKey('auth-token')) {
        next()
      } else { next({name: 'SignIn' }) }
    }
  },
  {
    path: '*',
    name: 'PageNotFound',
    component: PageNotFound,
  },
  {
    path: '/live-chat',
    name: 'LiveChat',
    component: LiveChat
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  scrollBehavior (to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { x:0, y: 0 }
    }
  }
})

export default router
