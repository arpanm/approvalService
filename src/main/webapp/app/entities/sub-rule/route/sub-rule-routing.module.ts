import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubRuleComponent } from '../list/sub-rule.component';
import { SubRuleDetailComponent } from '../detail/sub-rule-detail.component';
import { SubRuleUpdateComponent } from '../update/sub-rule-update.component';
import { SubRuleRoutingResolveService } from './sub-rule-routing-resolve.service';

const subRuleRoute: Routes = [
  {
    path: '',
    component: SubRuleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubRuleDetailComponent,
    resolve: {
      subRule: SubRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubRuleUpdateComponent,
    resolve: {
      subRule: SubRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubRuleUpdateComponent,
    resolve: {
      subRule: SubRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subRuleRoute)],
  exports: [RouterModule],
})
export class SubRuleRoutingModule {}
