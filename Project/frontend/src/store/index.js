import Vue from 'vue'
import Vuex from 'vuex'
import router from '../router'
import VueCookies from 'vue-cookies'
import checkView from 'vue-check-view'
import Axios from 'axios'
import firebase from 'firebase'

Vue.use(Vuex)
Vue.use(VueCookies)
Vue.use(checkView)
Vue.use(firebase)


export default new Vuex.Store({
  state: {
    api_server: 'http://localhost:8081/jara',      // Local
    // api_server: 'https://i3a308.p.ssafy.io/jara',  // Server
    authToken: VueCookies.get('auth-token'),
    entrance: true,
    drawer: false,
    group: null,
    userInfo: null,
    results: [],
    showSearch: false,
    users: [],
    psas: [],
    today: new Date(),
    week: ['일', '월', '화', '수', '목', '금', '토'],
    error: false,
    request: false,
    dialog: false,
    reportData: {
      accused_nickname: '',
      contents: '',
      reporter_id: ''
    },
    nickNameRules: {
      required: value => value.trim().length > 0 || '닉네임을 입력해주세요.'
    },
    contentsRules : {
      required: v => v.trim().length > 0 || '허위 신고는 제재당할 수 있습니다.'
    },
    requestData: {
      notification: [],
      request: []
    }
  },
  getters: {
    isLoggedIn: state => !!state.authToken,
    isEntrance: state => state.entrance,
    isDrawer: state => state.drawer,
    isSearch: state => state.showSearch,
    isError: state => state.error,
    isRequested: state => state.requestData.notification.length > 0 || state.requestData.request.length > 0,
    isDialog: state => state.dialog,
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.authToken = token
      VueCookies.set('auth-token', token)
    },
    SET_USERINFO(state, val) {
      state.userInfo = val
    },
    SET_NICKNAMES(state, val) {
      state.users = val
    },
    SET_PSAS(state, val) {
      state.psas = val
    },
    SET_DRAWER(state, status) {
      state.drawer = status
    },
    SET_ENTRANCE(state, status) {
      state.entrance = status
    }, 
    SET_SHOW(state, status) {
      state.showSearch = status
    },
    SET_ERROR(state, status) {
      state.error = status
    },
    SET_DIALOG(state, status) {
      state.dialog = status
    },
    SET_ALARM(state, status) {
      state.request = status
    },
    SET_REQUEST(state, val) {
      state.requestData.request.push(val)
    },
    SET_NOTIFICATION(state, val) {
      state.requestData.notification.push(val)
    },
    SET_CHECK(state) {
      state.requestData.notification = []
      firebase.database().ref(`comment/${state.userInfo.id}`).remove()
      firebase.database().ref(`liked/${state.userInfo.id}`).remove()
    },
    SET_DEQUEST(state, val) {
      state.requestData.request.splice(val, 1)
    },
    SET_INIT(state) {
      state.requestData.notification = []
      state.requestData.request = []
    },
  },
  actions: {
    signIn({ commit, state, dispatch }, data) {
      Axios.post(`${state.api_server}/accounts/signin`, data)
        .then(res => {
          commit('SET_TOKEN', res.headers['jwt-auth-token'])
          dispatch('getUser')
          dispatch('checkDB', res.data.id)
          router.push('/main')
        })
        .catch(() => {
          commit('SET_ERROR', true) 
        })
    },
    signOut({ commit }) {
      commit('SET_TOKEN', null)
      commit('SET_INIT')
      commit('SET_USERINFO', null)
      VueCookies.remove('auth-token')
      router.push('/accounts/signin')
    },
    getUsers({ commit, state }) {
      Axios.get(`${state.api_server}/accounts`)
        .then(res => {
          const imgs = {}
          const nicks = {}
          state.results.push({ header: '모든 유저' })
          state.results.push({ divider: true })
          res.data.forEach(function(user) {
            nicks[user.id] = user.nickname
            imgs[user.id] = user.img_src
            state.results.push({ nickname: user.nickname, id: user.id })
          })
          commit('SET_NICKNAMES', nicks)
          commit('SET_PSAS', imgs)
        })
    },
    draw({ commit, state }) {
      commit('SET_DRAWER', !state.drawer)
    },
    show({ commit, state }) {
      commit('SET_SHOW', !state.showSearch)
    },
    goToHome({ commit }) {
      router.push('/main')
      commit('SET_DRAWER', false)
    },
    goToBarters({ commit }) {
      router.push('/barters')
      commit('SET_DRAWER', false)
    },
    goToChecks({ commit }) {
      router.push('/checks')
      commit('SET_DRAWER', false)
    },
    goToEithers({ commit }) {
      router.push('/eithers')
      commit('SET_DRAWER', false)
    },
    goToTips({ commit }) {
      router.push('/tips')
      commit('SET_DRAWER', false)
    },
    goToProfile({ commit }) {
      router.push('/accounts/user')
      commit('SET_DRAWER', false)
    },
    report({ commit, getters, state }) {
      if (getters.isLoggedIn) {
        commit('SET_DRAWER', false)
        const requestHeaders = {
          headers: {
            token: VueCookies.get('auth-token')
          }
        }
        Axios.get(`${state.api_server}/reports/admin`, requestHeaders)
          .then(res => {
            if (res.data.isAdmin) {router.push('/admin')}
            else {commit('SET_DIALOG', true)}
          })
      } else {
        alert('로그인한 회원님만 사용할 수 있어요!')
        commit('SET_DRAWER', false)
      }
    },
    getUser({ commit, state }) {
      const requestHeaders = {
        headers : {
          token : VueCookies.get('auth-token'),
        }  
      }
      Axios.get(`${state.api_server}/accounts/info`, requestHeaders)
        .then(res => commit('SET_USERINFO', res.data))
        .catch(() => {
          commit('SET_TOKEN', null)
          VueCookies.remove('auth-token')
          alert('로그인 유효 시간이 만료되었습니다. 다시 로그인 해 주세요.')
          router.push('/accounts/signin')
        })
    },
    sendReport({ commit, state }) {
      const data = {
        accused_nickname: state.reportData.accused_nickname,
        contents: state.reportData.contents,
        reporter_id: state.userInfo.id
      }
      Axios.post(`${state.api_server}/reports/`, data)
      .then(() => {
        alert('신고 내역이 성공적으로 접수되었습니다.')
        state.reportData.accused_nickname = ''
        state.reportData.contents = ''
        commit('SET_DIALOG', false)
      })
      .catch(() => {
        alert('유효하지 않은 닉네임입니다.')
        state.reportData.accused_nickname = ''
        state.reportData.contents = ''
        commit('SET_DIALOG', false)
      })
    },
    checkDB({ commit }, id) {
      firebase.database().ref(`comment/${id}`).on('child_added', function(snapshot) {
        commit('SET_NOTIFICATION', snapshot.val())
      })
      firebase.database().ref(`liked/${id}`).on('child_added', function(snapshot) {
        commit('SET_NOTIFICATION', snapshot.val())
      })
      firebase.database().ref(`following/${id}`).on('child_added', function(snapshot) {
        commit('SET_REQUEST', snapshot.val())
      })
    },
  },
  modules: {
  }
})
