<template>
  <v-container fluid class="mt-5" style="font-family: 'Handon3gyeopsal300g';">
    <div class="text-sm-h3 text-h4 font-weight-bold ml-3" style="font-family: 'Handon3gyeopsal600g' !important;">꼼꼼한 자라<v-icon x-large class="ml-2">mdi-chevron-down-box-outline</v-icon></div>
    <div class="mt-5 ml-3">
      <v-chip
        @click="red"
        color="red darken-1"
        text-color="white"
      >
        집 구하기
        <v-icon right>mdi-home-group</v-icon>
      </v-chip>
      <v-chip
        @click="orange"
        color="orange darken-2"
        text-color="white"
      >
        입주
        <v-icon right>mdi-home-city</v-icon>
      </v-chip>
      <v-chip
        @click="yellow"
        color="yellow"
        text-color="white"
      >
        이사
        <v-icon right>mdi-truck-outline</v-icon>
      </v-chip>
      <v-chip
        @click="green"
        color="green darken-2"
        text-color="white"
      >
        자라
        <v-icon right>mdi-turtle</v-icon>
      </v-chip>
    </div>
    <v-divider class="my-5"></v-divider>
    <div v-if="isRed">
      <div class="px-3" v-for="(item, index) in seek" :key="index">
        <v-checkbox
          @click="save(item.id, item.checked)"
          v-model="item.checked"
          color="green darken-2"
          :label="item.item"
          :class="{ 'done' : item.checked }"
        ></v-checkbox>
      </div>
    </div>
    <div v-if="isOrange">
      <div class="px-3" v-for="(item, index) in moveIn" :key="index">
        <v-checkbox
          @click="save(item.id, item.checked)"
          v-model="item.checked"
          color="green darken-2"
          :label="item.item"
          :class="{ 'done' : item.checked }"
        ></v-checkbox>
      </div>
    </div>
    <div v-if="isYellow">
      <div class="px-3" v-for="(item, index) in moveOut" :key="index">
        <v-checkbox
          @click="save(item.id, item.checked)"
          v-model="item.checked"
          color="green darken-2"
          :label="item.item"
          :class="{ 'done' : item.checked }"
        ></v-checkbox>
      </div>
    </div>
    <div v-if="isGreen">
      <div v-if="self.length==0" class="text-center font-weight-bold">자라님의 체크리스트를 직접 만들어보세요.</div>
      <v-form class="mt-3 px-3 d-flex">
        <v-textarea
          v-model="checkItem"
          label="입력해주세요."
          color="green darken-1"
          auto-grow
          outlined
          rows="1"
          row-height="10"
        ></v-textarea>
        <div>
          <v-btn @click="addCheck" text class="mt-3 font-weight-bold" color="green darken-1">등록</v-btn>
        </div>
      </v-form>
      <div v-for="(item, idx) in self" :key="idx" class="px-3 d-flex justify-space-between align-center">
        <v-checkbox
          @click="customSave(item)"
          v-model="item.checked"
          color="green darken-2"
          :label="item.item"
          :class="{ 'done' : item.checked }"
        ></v-checkbox>
        <v-btn @click="del(idx)" icon><v-icon>mdi-close-circle</v-icon></v-btn>
      </div>
    </div> 
    <v-btn fab fixed small dark bottom right @click="scrollToTop" color="light-green">
      <v-icon>mdi-apple-keyboard-control</v-icon>
    </v-btn>
  </v-container>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'Checks',
  data() {
    return {
      isRed: true,
      isOrange: false,
      isYellow: false,
      isGreen: false,
      isValid: false,
      seek: [
        {
          id: 1,
          checked: false,
          item: '관리비 및 공과금 확인'
        },
        {
          id: 2,
          checked: false,
          item: '역세권 확인'
        },
        {
          id: 3,
          checked: false,
          item: '편의점 확인'
        },
        {
          id: 4,
          checked: false,
          item: '난방(보일러) 확인'
        },
        {
          id: 5,
          checked: false,
          item: '방음 확인'
        },
        {
          id: 6,
          checked: false,
          item: '싱크대 아래(배수관) 확인'
        },
        {
          id: 7,
          checked: false,
          item: '수압 확인'
        },
        {
          id: 8,
          checked: false,
          item: '곰팡이(습도) 확인'
        },
        {
          id: 9,
          checked: false,
          item: '채광 확인'
        },
        {
          id: 10,
          checked: false,
          item: '옵션 확인'
        },
        {
          id: 11,
          checked: false,
          item: '보안 확인'
        }
      ],
      moveIn: [
        {
          id: 12,
          checked: false,
          item: '전입 신고'
        },
        {
          id: 13,
          checked: false,
          item: '이사 업체 계약'
        },
        {
          id: 14,
          checked: false,
          item: '엘레베이터 사용 시 사전 예약'
        },
        {
          id: 15,
          checked: false,
          item: '디지털 도어락 비밀번호 변경'
        },
        {
          id: 16,
          checked: false,
          item: '주차증 발급'
        },
        {
          id: 17,
          checked: false,
          item: '은행, 신용카드 주소 변경'
        },
        {
          id: 18,
          checked: false,
          item: '도시가스 신청'
        },
        {
          id: 19,
          checked: false,
          item: '인터넷, 케이블TV 신청'
        },
      ],
      moveOut: [
        {
          id: 20,
          checked: false,
          item: '공과금 정산'
        },
        {
          id: 21,
          checked: false,
          item: '관리비 납부 확인'
        },
        {
          id: 22,
          checked: false,
          item: '우편물 주거이전 서비스 신청'
        },
        {
          id: 23,
          checked: false,
          item: '신문, 우유 등 배달 중지 요청'
        },
        {
          id: 24,
          checked: false,
          item: '도시가스 철거'
        },
        {
          id: 25,
          checked: false,
          item: '세탁소 등 이용품목 수령 및 확인'
        },
        {
          id: 26,
          checked: false,
          item: '냉장고 안 식품 정리'
        },
        {
          id: 27,
          checked: false,
          item: '인터넷, 케이블 TV 이전 설치 요청'
        },
        {
          id: 28,
          checked: false,
          item: '폐기물 스티커 구입'
        },
      ],
      self: [],
      checkItem: '',
      cnt: 29
    }
  },
  methods: {
    red() {
      this.isRed = true
      this.isOrange = false
      this.isYellow = false
      this.isGreen = false
    },
    orange() {
      this.isRed = false
      this.isOrange = true
      this.isYellow = false
      this.isGreen = false
    },
    yellow() {
      this.isRed = false
      this.isOrange = false
      this.isYellow = true
      this.isGreen = false
    },
    green() {
      this.isRed = false
      this.isOrange = false
      this.isYellow = false
      this.isGreen = true
    },
    addCheck() {
      if (this.checkItem.trim() <= 0) {
        alert('유효한 입력이 아닙니다.')
        this.checkItem = ''
      }
      else {
        const data = {
          id: this.cnt,
          item: this.checkItem,
          checked: false
        }
        this.self.push(data)
        this.checkItem = ''
        localStorage.setItem(data.id, `${data.item}:${data.checked}`)
        this.cnt++
        localStorage.setItem('custom', `${this.cnt}`)
      }
    },
    del(index) {
      this.self.splice(index, 1)
      localStorage.removeItem(this.cnt-1)
      this.cnt--
      localStorage.setItem('custom', `${this.cnt}`)
    },
    save(id, status) {
      if (status) {
        localStorage.setItem(id, `${status}`)
      } else {
        localStorage.removeItem(id)
      }
    },
    customSave(data) {
      localStorage.setItem(data.id, `${data.item}:${data.checked}`)
    },
    load() {
      const current = localStorage.getItem('custom')
      if (current != null) {this.cnt = current}
      for (let i=1; i < this.cnt; i++) {
        const status = localStorage.getItem(i)
        if (status != null) {
          if (1 <= i && i <= 11) {
            this.seek[i-1].checked = status
          } else if (12 <= i && i <= 19) {
            this.moveIn[i-12].checked = status
          } else if (20 <= i && i <= 28) {
            this.moveOut[i-20].checked = status
          } else {
            const result = status.split(':')
            const item = result[0]
            let checked = false
            if (result[1] == 'true') { checked = true }
            const custom = {
              id : i,
              item: item,
              checked: checked
            }
            this.self.push(custom)
          }
        }
      }
    },
    scrollToTop() {
      this.$vuetify.goTo(0)
    }
  },
  created() {
    this.load()
  },
  computed: {
    ...mapState([
      'userInfo'
    ])
  }
}
</script>

<style scoped>
  .done {
    text-decoration: line-through;
  }
</style>