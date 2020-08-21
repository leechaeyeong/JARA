<template>
  <v-container fluid style="font-family: 'Handon3gyeopsal300g';">
    <div class="mt-5">
      <div class="mx-3 font-weight-bold text-sm-h3 text-h4" style="font-family: 'Handon3gyeopsal600g' !important;">
        알뜰한 자라
        <v-icon x-large>mdi-shopping-outline</v-icon>
      </div>
      <div>
        <v-flex class="sm6 offset-sm3 justify-center mt-5">
          <v-text-field
            label="검색어를 입력해주세요."
            outlined
            append-icon="mdi-magnify"
            v-model="search"
            class="col-12 px-3"
            color="green darken-2"
          >
          </v-text-field>
        </v-flex>
      </div>
      <!-- <div align="center" justify="center">
        <BartersItem
          v-for="(barter, index) in barters"
          :key="index"
          :barter="barter"
        />
      </div> -->
      <!-- <v-btn @click="write" style="position: fixed; bottom:3vh; right: 3vh" color="primary" fab small dark absolute bottom right>
        <v-icon>mdi-pencil</v-icon>
      </v-btn> -->
      <v-container>
        <v-layout row wrap>
          <v-flex xs12>
            <v-data-table
              color="green darken-2"
              :headers="headers"
              :items="barters"
              :loading="loading"
              :page.sync="page"
              :items-per-page="5"
              hide-default-footer
              class="elevation-1"
              @page-count="pageCount = $event"
              :search="search"
              :custom-filter="filterSearch"
              @click:row="goToBarterDetail"
            >
              <template v-slot:item.status="{ item }">
                <v-chip :color="getColor(item.status)" dark>
                  {{ getStatus(item.status) }}
                </v-chip>
              </template>
            </v-data-table>
            <div class="text-center py-2">
              <v-pagination v-model="page" :length="pageCount" color="green darken-2"></v-pagination>
            </div>
          </v-flex>
        </v-layout>
      </v-container>
    </div>
    <div class="pb-5"></div>
    <v-dialog v-model="dialog" max-width="500px">
      <template v-slot:activator="{ on, attrs}">
        <v-spacer></v-spacer>
        <v-btn
          fab
          small
          bottom
          right
          fixed
          class="my-1"
          v-bind="attrs"
          v-on="on"
          color="green lighten-1"
        >
          <v-icon color="white">mdi-pencil</v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline" style="font-family: 'Handon3gyeopsal600g' !important;">새 글 쓰기</span>
        </v-card-title>

        <v-card-text>
          <v-container style="font-family: 'Handon3gyeopsal300g' !important;"> 
            <v-row>
              <v-col cols="12" sm="6" md="4">
                <v-text-field color="green darken-2" v-model="editedItem.title" label="제목" placeholder="제목을 입력해 주세요." required :rules="[() => !!editedItem.title || '필수 입력입니다.']"></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-img color="green darken-2" v-if="file != null" :src="imageURL"></v-img>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-file-input
                  @change="image"
                  v-model="file"
                  placeholder="사진을 첨부해 주세요."
                  color="green darken-2"
                >
                </v-file-input>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-text-field color="green darken-2" v-model="editedItem.contents" label="내용" placeholder="내용을 입력해 주세요." required :rules="[() => !!editedItem.contents || '필수 입력입니다.']"></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-text-field color="green darken-2" v-model="editedItem.price" label="가격"></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="4">
                <v-autocomplete
                  label="분류"
                  placeholder="분류를 선택해주세요."
                  v-model="tag"
                  :items="items"
                  color="green darken-2"
                  item-color="green darken-2"
                  required
                  :rules="[() => !!tag || '필수 입력입니다.']"
                ></v-autocomplete>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="close">취소</v-btn>
          <v-btn color="green darken-2" text @click="save">저장</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios'
// import BartersItem from '../components/Barters/BartersItem.vue'
import _ from 'lodash'
import { mapState } from 'vuex'
import firebase from 'firebase'

export default {
  name: 'Barters',
  // components: {
  //   BartersItem,
  // },
  data() {
    return {
      barters: [],
      headers: [
        { text: '날짜', value: 'created_at', sortable: true },
        { text: '제목', value: 'title', sortable: false },
        { text: '가격', value: 'price', sortable: true },
        { text: '분류', value: 'tag', sortable: true },
        { text: '상태', value: 'status', sortable: true },
      ],
      loading: false,
      search: '',
      page: 1,
      pageCount: 0,
      dialog: false,
      file: null,
      imageURL: '',
      img_src: '',
      id: null,
      editedItem: {
        title: '',
        tag_id: null,
        contents: '',
        writer: this.$store.state.userInfo.id,
        price: 0,
      },
      defaultItem: {
        title: '',
        tag_id: null,
        contents: '',
        writer: this.$store.state.userInfo.id,
        price: 0,
      },
      tag: '',
      items: ['구해요','사요','팔아요','나눠요'],
    }
  },
  computed: {
    ...mapState([
      'api_server',
      'userInfo',
    ])
  },
  watch: {
    dialog(val) {
      val || this.close()
    }
  },
  created() {
    this.loading = true
    axios.get(`${this.$store.state.api_server}/barters`)
      .then(res => {
        this.barters = res.data
        this.barters.forEach(function(item) {
          const tags = {
            5 : '구해요',
            6 : '사요',
            7 : '팔아요',
            8 : '나눠요'
          }
          item['tag'] = tags[item.tag_id]
          item['price'] = item.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
        })
        this.loading = false
      })
      .catch(() => {
        this.loading = false
      })
  },
  methods: {
    image() {
      this.imageURL = URL.createObjectURL(this.file)
    },
    getColor(s) {
      if (s) return 'gray'
      else return 'green'
    },
    getStatus(s) {
      if (s) return '완료'
      else return '진행 중'
    },
    filterSearch(value, search) {
      return value != null && search !=null &&
        typeof value ==='string' && value.toString().indexOf(search) !== -1
    },
    close() {
      this.dialog = false
      this.file = null
      this.imageURL = ''
      this.img_src = ''
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
      })
    },
    save() {
      const tag_id_dict = {'구해요':5,'사요':6,'팔아요':7,'나눠요':8}
      this.editedItem.tag_id = tag_id_dict[this.tag]

      axios.post(`${this.$store.state.api_server}/barters`,this.editedItem)
        .then(res => {
          this.id = res.data.id
          const temp = res.data
          const tags = {
            5 : '구해요',
            6 : '사요',
            7 : '팔아요',
            8 : '나눠요'
          }
          temp['tag'] = tags[res.data.tag_id]
          temp['price'] = res.data.price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
          if (this.file) {
            temp.img_src = this.uploadImg()
          } else { alert('거래가 성공적으로 등록되었습니다.')}
          this.barters.push(temp)
          this.barters = _.orderBy(this.barters,'id','desc')
          this.close()
        })
    },
    goToBarterDetail(val) {
      this.$router.push(`/barters/${val.id}`)
    },
    uploadImg() {
      firebase.storage().ref(`barters/${this.id}`).put(this.file)
        .then(() => {
          firebase.storage().ref(`barters/${this.id}`).getDownloadURL()
            .then(url => {
              axios.put(`${this.$store.state.api_server}/barters/${this.id}/img`,{ img_src : url })
                .then(() => alert('거래를 성공적으로 등록했습니다.'))
            })
        })
    }
  }
}
</script>

<style scoped>
  #barters {
    height: 100%;
  }
  #working {
    padding-top: 20vh;
  }
</style>